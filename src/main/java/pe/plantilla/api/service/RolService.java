package pe.plantilla.api.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import pe.plantilla.api.model.Rol;
import pe.plantilla.api.model.Usuario;
import pe.plantilla.api.repository.RolRepository;
import pe.plantilla.api.utils.ApiException;

@Service
public class RolService extends BasicService<RolRepository, Rol> implements IRolService {

	@Autowired
	private IUsuarioService userService;
	
	@Override
	public List<Rol> getRolesByPerfil(int perfil) throws ApiException {
		log.info("Obteniendo roles del perfil {}", perfil);
		try {
			return repository.getRolesByPerfil(perfil);
		} catch (SQLException e) {
			log.error("Error sql al obtener roles del perfil {} - {} - {}",perfil,e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}

	@Override
	public List<Rol> getRolesByUsuario(String usuario) throws ApiException {
		log.info("Obteniendo roles del usuario {}", usuario);
		try {
			return repository.getRolesByUsuario(usuario);
		} catch (SQLException e) {
			log.error("Error sql al obtener roles por usuario {} - {} - {}",usuario,e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}
	
	@Override
	public List<Rol> getRolesByUsuarioModulo(String usuario,int modulo) throws ApiException {
		log.info("Obteniendo roles del usuario - modulo {} - {}", usuario, modulo);
		try {
			return repository.getRolesByUsuarioModulo(usuario,modulo);
		} catch (SQLException e) {
			log.error("Error sql al obtener roles por usuario y módulo {} - {} - {} - {}",usuario,modulo,e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}

	}

	@Override
	public List<String> getAuthorities(String usuario) throws ApiException {
		List<Rol> roles;
		List<String> auths;
		Usuario user;
		try {
			user = userService.findByUsername(usuario);
			if(null == user)
				throw new ApiException(HttpStatus.UNAUTHORIZED,"Usuario no autorizado, no existe usuario.");
			
			roles = getRolesByUsuario(usuario);
			if(null == roles)
				throw new ApiException(HttpStatus.UNAUTHORIZED,"Usuario no autorizado, no tiene roles.");
			
			auths = new ArrayList<String>();
			roles.forEach((rol) -> auths.add(rol.getAuthority()));
		} catch (ApiException e) {
			throw e;
		}
		return auths;
	}

}
