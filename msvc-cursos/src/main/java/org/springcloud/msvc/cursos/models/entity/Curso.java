package org.springcloud.msvc.cursos.models.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springcloud.msvc.cursos.models.Usuario;

@Entity
@Table(name = "cursos")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String nombre;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id")
	private List<CursoUsuario> cursoUsuarios;

	@Transient
	private List<Usuario> usuarios;

	public Curso()
	{
		super();
		cursoUsuarios = new ArrayList<>();
		usuarios = new ArrayList<>();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getNombre()
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public List<CursoUsuario> getCursoUsuarios()
	{
		return this.cursoUsuarios;
	}

	public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios)
	{
		this.cursoUsuarios = cursoUsuarios;
	}

	public void addCursoUsuario(CursoUsuario cursoUsuario)
	{
		cursoUsuarios.add(cursoUsuario);
	}

	public void removeCursoUsuario(CursoUsuario cursoUsuario)
	{
		cursoUsuarios.remove(cursoUsuario);
	}

	public List<Usuario> getUsuarios()
	{
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios)
	{
		this.usuarios = usuarios;
	}

}
