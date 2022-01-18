package pe.plantilla.api.controller;

import static pe.plantilla.api.utils.Util.respuestaApi;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;

import pe.plantilla.api.model.BasicEntity;
import pe.plantilla.api.service.IBasicService;
import pe.plantilla.api.utils.ApiException;
import pe.plantilla.api.utils.Util;

public class BasicController<T extends BasicEntity,E extends IBasicService<T>>{
	
	protected final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	protected E service;
	
	@PostMapping
	public ResponseEntity<?> saveEntity(@RequestBody T entity) {
		logger.info("Se recibió entidad - {}",Util.objectToJson(entity));
		try {
			service.saveEntity(entity);
			return respuestaApi(null, "Transacción OK.", HttpStatus.OK);
		} catch (ApiException e) {
			return respuestaApi(null, e.getMessage(), e.getStatus());
		}
	}
	
	@GetMapping
	public ResponseEntity<?> searchEntity(@RequestParam Map<String, String> params) {
		logger.info("Obteniendo entidades por filtro");
		try {
			Object rpta = service.searchEntity(params);
			return respuestaApi(rpta, "Transacción OK.", HttpStatus.OK);
		}catch (ApiException e) {
			return respuestaApi(null, e.getMessage(), e.getStatus());
		} 
	}
	
	@GetMapping(path = "/pag")
	public ResponseEntity<?> pagingEntitys(@RequestParam Map<String, String> params) {
		logger.info("Obteniendo filtro parmas - {}",params);
		try {
			PageInfo<T> rpta = service.pagingEntitys(params);
			return respuestaApi(rpta, "Transacción OK.", HttpStatus.OK);
		}catch (ApiException e) {
			return respuestaApi(null, e.getMessage(), e.getStatus());
		} 
	}
	
	@PutMapping
	public ResponseEntity<?> updateEntity(@RequestBody T entity) {
		logger.info("Se recibió entidad - {}",Util.objectToJson(entity));
		try {
			service.updateEntity(entity);
			logger.info("Se procesó correctamente solicitud actualizar");
			return respuestaApi(null, "Transacción OK.", HttpStatus.OK);
		} catch (ApiException e) {
			return respuestaApi(null, e.getMessage(),e.getStatus());
		} 
	}
	
	@DeleteMapping(path = "/{codigo}")
	public ResponseEntity<?> deleteEntity(@PathVariable int codigo) {
		logger.info("Se recibio código  - {}",codigo);
		try {
			service.deleteEntity(codigo);
			logger.info("Se procesó correctamente solicitud deshabilitar  - {}",codigo);
			return respuestaApi(null, "Transacción OK.", HttpStatus.OK);
		} catch (ApiException e) {
			return respuestaApi(null, e.getMessage(), e.getStatus());
		}
	}
	
}
