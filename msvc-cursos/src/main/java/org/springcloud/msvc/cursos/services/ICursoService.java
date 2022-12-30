package org.springcloud.msvc.cursos.services;

import java.util.List;
import java.util.Optional;

import org.springcloud.msvc.cursos.models.Usuario;
import org.springcloud.msvc.cursos.models.entity.Curso;

public interface ICursoService {

	List<Curso> findAll();

	Optional<Curso> getById(Long id);

	Optional<Curso> getByIdWithUsers(Long id, String token);

	Curso save(Curso curso);

	void delete(Long id);

	Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);

	Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);

	Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);

	void eliminarCursoUsuarioById(Long id);

}
