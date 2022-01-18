package pe.plantilla.api.service;

import java.util.List;

import pe.plantilla.api.model.Rol;
import pe.plantilla.api.utils.ApiException;

public interface IRolService extends IBasicService<Rol>{
	
	List<Rol> getRolesByPerfil(int perfil) throws ApiException;
	
	List<Rol> getRolesByUsuario(String usuario) throws ApiException;
	
	List<Rol> getRolesByUsuarioModulo(String usuario,int modulo) throws ApiException;
	
	List<String> getAuthorities(String usuario) throws ApiException;
	
}
