package org.springcloud.msvc.usuarios.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springcloud.msvc.usuarios.models.services.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Environment env;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/crash")
	public void crash(){
		((ConfigurableApplicationContext) context).close();
	}

	@GetMapping
	public ResponseEntity<?> listar()
	{
		Map<String, Object> body = new HashMap<>();
		body.put("users", usuarioService.findAll());
		body.put("podinfo", env.getProperty("MY_POD_NAME") + ": "+env.getProperty("MY_POD_IP"));
		body.put("texto", env.getProperty("config.texto"));
		//return Collections.singletonMap("users", usuarioService.findAll());

		return ResponseEntity.ok(body);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalle(@PathVariable Long id)
	{
		Optional<Usuario> usuarioOptional = usuarioService.getById(id);
		if (usuarioOptional.isPresent())
		{
			return ResponseEntity.ok(usuarioOptional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	// @ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result)
	{
		if (result.hasErrors())
		{
			return validar(result);
		}
		if (!usuario.getEmail().isEmpty() && usuarioService.existsByEmail(usuario.getEmail()))
		{
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico"));
		}
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id)
	{
		if (result.hasErrors())
		{
			return validar(result);
		}
		Optional<Usuario> usuarioOptional = usuarioService.getById(id);
		if (usuarioOptional.isPresent())
		{
			Usuario userDB = usuarioOptional.get();

			if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(userDB.getEmail())
					&& usuarioService.findByEmail(usuario.getEmail()).isPresent())
			{
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico"));
			}

			userDB.setNombre(usuario.getNombre());
			userDB.setEmail(usuario.getEmail());
			userDB.setPassword(passwordEncoder.encode(usuario.getPassword()));

			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(userDB));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrar(@PathVariable Long id)
	{
		Optional<Usuario> usuarioOptional = usuarioService.getById(id);
		if (usuarioOptional.isPresent())
		{
			usuarioService.delete(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/usuarios-por-curso")
	public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids)
	{
		return ResponseEntity.ok(usuarioService.listarPorIds(ids));
	}

	private ResponseEntity<Map<String, String>> validar(BindingResult result)
	{
		Map<String, String> errores = new HashMap<>();
		result.getFieldErrors().forEach(fieldError ->
		{
			errores.put(fieldError.getField(),
					"El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errores);
	}

	@GetMapping("/authorized")
	public Map<String, Object> authorized(@RequestParam(name = "code") String code){
		return Collections.singletonMap("code", code);
	}

	@GetMapping("/login")
	public ResponseEntity<?> loginByEmail(@RequestParam(name = "email") String email) {
		Optional<Usuario> usuario = usuarioService.buscarPorEmail(email);
		if (usuario.isPresent())
			return ResponseEntity.ok(usuario);

		return ResponseEntity.notFound().build();
	}

}
