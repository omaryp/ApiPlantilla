package pe.plantilla.api.model;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;

public class Rol extends BasicEntity implements GrantedAuthority{
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message="Atributo nombre Rol no debe ser vacío.")
	private String nombre;
	private String descripcion;
	
	@Min(value=1, message="Atributo módulo debe ser != 0")
	@Digits(fraction = 0, integer = 11, message="Atributo módulo debe contener solo números")
	private int modulos_id;
	
	@NotBlank(message="Atributo icono no debe ser vacío.")
	@NotNull(message="Atributo icono no debe ser vacío.")
	private String icono;
	
	private boolean menu;
	
	@NotBlank(message="Atributo url menu no debe ser vacío.")
	private String url;
	
	@NotBlank(message="Atributo nombre menu no debe ser vacío.")
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

	@Override
	public String getAuthority() {
		return nombre;
	}

	public int getModulos_id() {
		return modulos_id;
	}

	public void setModulos_id(int modulos_id) {
		this.modulos_id = modulos_id;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public boolean isMenu() {
		return menu;
	}

	public void setMenu(boolean menu) {
		this.menu = menu;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNombre_menu() {
		return nombre_menu;
	}

	public void setNombre_menu(String nombre_menu) {
		this.nombre_menu = nombre_menu;
	}
	
}
