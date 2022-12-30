package org.springcloud.msvc.usuarios.models.services;

import java.util.List;
import java.util.Optional;

import org.springcloud.msvc.usuarios.models.entity.Usuario;

public interface IUsuarioService {

	List<Usuario> findAll();

	Optional<Usuario> getById(Long id);

	Usuario save(Usuario usuario);

	void delete(Long id);

	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> buscarPorEmail(String email);

	boolean existsByEmail(String email);

	List<Usuario> listarPorIds(List<Long> ids);
}
