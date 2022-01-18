package pe.plantilla.api.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import pe.plantilla.api.model.Modulo;
import pe.plantilla.api.repository.ModuloRepository;
import pe.plantilla.api.utils.ApiException;

@Service
public class ModuloService extends BasicService<ModuloRepository, Modulo> implements IModuloService {
	

	@Override
	public List<Modulo> getModulosByUsuario(String username) throws ApiException{
		try {
			return repository.getModulosByUsuario(username);
		}	catch (SQLException e) {
			log.error("Error sql al obtener módulos, búqueda por usuario {} - {}",e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}

}
