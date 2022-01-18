package pe.plantilla.api.service;

import java.util.List;

import pe.plantilla.api.model.Modulo;
import pe.plantilla.api.utils.ApiException;

public interface IModuloService extends IBasicService<Modulo> {
	
	List<Modulo> getModulosByUsuario(String usuario) throws ApiException;

}
