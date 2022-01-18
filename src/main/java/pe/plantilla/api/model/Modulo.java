package pe.plantilla.api.model;

import javax.validation.constraints.NotBlank;

public class Modulo extends BasicEntity {
	
	@NotBlank(message="Atributo nombre del modulo no debe ser vacío.")
	private String nombre;
	private String descripcion;
	@NotBlank(message="Atributo ícono no debe ser vacío.")
	private String icono;
	@NotBlank(message="Atributo nombre menu del módulo no debe ser vacío.")
	private String nombre_menu;

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

	public String getNombre_menu() {
		return nombre_menu;
	}

	public void setNombre_menu(String nombre_menu) {
		this.nombre_menu = nombre_menu;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

}
