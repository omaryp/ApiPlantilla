package pe.plantilla.api.controller;

import static pe.plantilla.api.utils.Util.respuestaApi;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import pe.plantilla.api.model.Perfil;
import pe.plantilla.api.model.RolPerfil;
import pe.plantilla.api.service.IPerfilService;
import pe.plantilla.api.utils.ApiException;
import pe.plantilla.api.utils.Util;

@RestController
@RequestMapping("/perfil")
public class PerfilApi extends BasicController<Perfil, IPerfilService> {
	
	
	@PostMapping("/rol")
	public ResponseEntity<?> saveRolesPerfil(@RequestBody List<RolPerfil> entitys) {
		logger.info("Se recibió entidad - {}",Util.objectToJson(entitys));
		try {
			service.guardarRolesPerfil(entitys);
			return respuestaApi(null, "Transacción OK.", HttpStatus.OK);
		} catch (ApiException e) {
			logger.error("Error de api al procesar peticion guardar - {} - {}",e.getMessage(),e);
			return respuestaApi(null, e.getMessage(),e.getStatus());
		}
	}
	
	@PutMapping("/rol")
	public ResponseEntity<?> deleteRolesPerfil(@RequestBody List<RolPerfil> entitys) {
		logger.info("Se recibió entidad - {}",Util.objectToJson(entitys));
		try {
			service.eliminarRolesPerfil(entitys);
			return respuestaApi(null, "Transacción OK.", HttpStatus.OK);
		} catch (ApiException e) {
			logger.error("Error de api al procesar peticion eliminar - {} - {}",e.getMessage(),e);
			return respuestaApi(null, e.getMessage(),e.getStatus());
		}
	}
}