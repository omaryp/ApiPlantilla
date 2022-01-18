package pe.plantilla.api.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import pe.plantilla.api.model.Perfil;
import pe.plantilla.api.model.RolPerfil;
import pe.plantilla.api.repository.PerfilRepository;
import pe.plantilla.api.utils.ApiException;

@Service
public class PerfilService extends BasicService<PerfilRepository, Perfil> implements IPerfilService {
	

	@Override
	public void guardarRolesPerfil(List<RolPerfil> entitys) throws ApiException {
		log.info("Eliminando roles del perfil {}", entitys);
		try {
			repository.guardarRolesPerfil(entitys);
		} catch (SQLException e) {
			log.error("Error sql al guardar roles a su perfil {} - {}",e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}
	
	@Override
	public void eliminarRolesPerfil(List<RolPerfil> entitys) throws ApiException {
		log.info("Eliminando roles del perfil {}", entitys);
		try {
			for (RolPerfil rolPerfil : entitys) {
				repository.deleteRolPerfil(rolPerfil);
			}
		} catch (SQLException e) {
			log.error("Error sql al eliminar roles a su perfil {} - {}",e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}

}
