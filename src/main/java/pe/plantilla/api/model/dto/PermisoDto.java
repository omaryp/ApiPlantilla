package pe.plantilla.api.model.dto;

import java.util.List;

import pe.plantilla.api.model.Modulo;
import pe.plantilla.api.model.Rol;

public class PermisoDto {

	private Modulo modulo;
	private List<Rol> roles;
	
	public Modulo getModulo() {
		return modulo;
	}
	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}
	public List<Rol> getRoles() {
		return roles;
	}
	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	
}
