package org.springcloud.msvc.cursos.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springcloud.msvc.cursos.models.Usuario;
import org.springcloud.msvc.cursos.models.entity.Curso;
import org.springcloud.msvc.cursos.services.ICursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import feign.FeignException;

@RestController
public class CursoController {

	@Autowired
	private ICursoService cursoService;

	@GetMapping
	public List<Curso> listar()
	{
		return cursoService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> detalle(@PathVariable Long id,
									 @RequestHeader(value = "Authorization", required = true) String token)
	{
		Optional<Curso> cursoOptional = cursoService.getByIdWithUsers(id, token);
		// cursoService.getById(id);
		if (cursoOptional.isPresent())
		{
			return ResponseEntity.ok(cursoOptional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> crear(@RequestBody Curso curso, BindingResult result)
	{
		if (result.hasErrors())
		{
			return validar(result);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(curso));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@RequestBody Curso curso, BindingResult result, @PathVariable Long id)
	{
		if (result.hasErrors())
		{
			return validar(result);
		}
		Optional<Curso> cursoOptional = cursoService.getById(id);
		if (cursoOptional.isPresent())
		{
			Curso cursoDB = cursoOptional.get();
			cursoDB.setNombre(curso.getNombre());

			return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.save(cursoDB));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> borrar(@PathVariable Long id)
	{
		Optional<Curso> cursoOptional = cursoService.getById(id);
		if (cursoOptional.isPresent())
		{
			cursoService.delete(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/asignar-usuario/{cursoId}")
	public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId)
	{
		Optional<Usuario> optional;
		try
		{
			optional = cursoService.asignarUsuario(usuario, cursoId);
		}
		catch (FeignException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
					"No existe el usuario por el id o error en la comunicación: " + e.getMessage()));
		}
		if (optional.isPresent())
		{
			return ResponseEntity.status(HttpStatus.CREATED).body(optional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/crear-usuario/{cursoId}")
	public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId)
	{
		Optional<Usuario> optional;
		try
		{
			optional = cursoService.crearUsuario(usuario, cursoId);
		}
		catch (FeignException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
					"No se pudo crear el usuario o error en la comunicación: " + e.getMessage()));
		}
		if (optional.isPresent())
		{
			return ResponseEntity.status(HttpStatus.CREATED).body(optional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/eliminar-usuario/{cursoId}")
	public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId)
	{
		Optional<Usuario> optional;
		try
		{
			optional = cursoService.eliminarUsuario(usuario, cursoId);
		}
		catch (FeignException e)
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("mensaje",
					"No se pudo eliminar el usuario por el id o error en la comunicación: " + e.getMessage()));
		}
		if (optional.isPresent())
		{
			return ResponseEntity.status(HttpStatus.OK).body(optional.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/eliminar-curso-usuario/{id}")
	public ResponseEntity<?> eliminarCursoUsuario(@PathVariable Long id)
	{
		cursoService.eliminarCursoUsuarioById(id);
		return ResponseEntity.noContent().build();
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
}
