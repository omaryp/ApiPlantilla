package pe.plantilla.api.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;

import pe.plantilla.api.model.Usuario;
import pe.plantilla.api.model.dto.UsuarioSessionDto;
import pe.plantilla.api.utils.ApiException;

public interface IUsuarioService extends IBasicService<Usuario>{

	Usuario findByUsername(String username) throws ApiException;
	
	void disabledUserbyUsername(String username) throws ApiException;

	String getUserToken();
	
	UsuarioSessionDto getDatosSession() throws ApiException;

	void guardarUsuario(Usuario entity) throws ApiException;

	void actualizarUsuario(Usuario entity) throws ApiException;

	PageInfo<Usuario> paginandoUsuario(Map<String, String> params) throws ApiException;

}
