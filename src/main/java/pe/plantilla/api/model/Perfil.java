package pe.plantilla.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Perfil extends BasicEntity{

	@NotBlank(message="Atributo nombre Perfil no debe ser vacío.")
	@NotNull(message="Atributo nombre Perfil no debe ser vacío.")
	private String nombre;
	private String descripcion;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
