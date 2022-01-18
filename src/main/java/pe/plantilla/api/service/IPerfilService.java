package pe.plantilla.api.service;

import java.util.List;

import pe.plantilla.api.model.Perfil;
import pe.plantilla.api.model.RolPerfil;
import pe.plantilla.api.utils.ApiException;

public interface IPerfilService extends IBasicService<Perfil> {
	
	void guardarRolesPerfil(List<RolPerfil> entitys) throws ApiException;
	void eliminarRolesPerfil(List<RolPerfil> entitys) throws ApiException;
}
