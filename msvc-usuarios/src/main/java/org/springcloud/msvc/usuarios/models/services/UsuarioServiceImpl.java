package org.springcloud.msvc.usuarios.models.services;

import java.util.List;
import java.util.Optional;

import org.springcloud.msvc.usuarios.clients.CursoClientRest;
import org.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springcloud.msvc.usuarios.models.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private CursoClientRest clientRest;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll()
	{
		return (List<Usuario>) usuarioRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> getById(Long id)
	{
		return usuarioRepository.findById(id);
	}

	@Override
	@Transactional
	public Usuario save(Usuario usuario)
	{
		return usuarioRepository.save(usuario);
	}

	@Override
	@Transactional
	public void delete(Long id)
	{
		usuarioRepository.deleteById(id);
		clientRest.eliminarCursoUsuario(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> findByEmail(String email)
	{
		return usuarioRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmail(String email)
	{
		return usuarioRepository.buscarPorEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email)
	{
		return usuarioRepository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> listarPorIds(List<Long> ids)
	{
		return (List<Usuario>) usuarioRepository.findAllById(ids);
	}
}
