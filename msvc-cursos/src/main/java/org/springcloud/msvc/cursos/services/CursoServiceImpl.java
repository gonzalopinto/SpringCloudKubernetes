package org.springcloud.msvc.cursos.services;

import java.util.List;
import java.util.Optional;

import org.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.springcloud.msvc.cursos.models.Usuario;
import org.springcloud.msvc.cursos.models.entity.Curso;
import org.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoServiceImpl implements ICursoService {

	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private UsuarioClientRest clientRest;

	@Override
	@Transactional(readOnly = true)
	public List<Curso> findAll()
	{
		return (List<Curso>) cursoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Curso> getById(Long id)
	{
		return cursoRepository.findById(id);
	}

	@Override
	@Transactional
	public Curso save(Curso curso)
	{
		return cursoRepository.save(curso);
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		cursoRepository.deleteById(id);
	}

	@Override
	@Transactional
	public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId)
	{
		Optional<Curso> optional = cursoRepository.findById(cursoId);
		if (optional.isPresent())
		{
			Usuario usuarioMsvc = clientRest.detalle(usuario.getId());

			Curso curso = optional.get();
			CursoUsuario cursoUsuario = new CursoUsuario();
			cursoUsuario.setUsuarioId(usuarioMsvc.getId());

			curso.addCursoUsuario(cursoUsuario);
			cursoRepository.save(curso);
			return Optional.of(usuarioMsvc);
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId)
	{
		Optional<Curso> optional = cursoRepository.findById(cursoId);
		if (optional.isPresent())
		{
			Usuario usuarioNuevoMsvc = clientRest.crear(usuario);

			Curso curso = optional.get();
			CursoUsuario cursoUsuario = new CursoUsuario();
			cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

			curso.addCursoUsuario(cursoUsuario);
			cursoRepository.save(curso);
			return Optional.of(usuarioNuevoMsvc);
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId)
	{
		Optional<Curso> optional = cursoRepository.findById(cursoId);
		if (optional.isPresent())
		{
			Usuario usuarioMsvc = clientRest.detalle(usuario.getId());

			Curso curso = optional.get();
			CursoUsuario cursoUsuario = new CursoUsuario();
			cursoUsuario.setUsuarioId(usuarioMsvc.getId());

			curso.removeCursoUsuario(cursoUsuario);
			cursoRepository.save(curso);
			return Optional.of(usuarioMsvc);
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public Optional<Curso> getByIdWithUsers(Long id, String token)
	{
		Optional<Curso> o = cursoRepository.findById(id);
		if (o.isPresent())
		{
			Curso curso = o.get();
			if (!curso.getCursoUsuarios().isEmpty())
			{
				List<Long> ids = curso.getCursoUsuarios().stream().map(cu -> cu.getUsuarioId()).toList();

				List<Usuario> usuarios = clientRest.obtenerAlumnosPorCurso(ids, token);
				curso.setUsuarios(usuarios);
			}
			return Optional.of(curso);
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public void eliminarCursoUsuarioById(Long id)
	{
		cursoRepository.eliminarCursoUsuario(id);
	}
}
