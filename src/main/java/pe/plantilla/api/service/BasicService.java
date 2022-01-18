package pe.plantilla.api.service;

import static pe.plantilla.api.utils.Util.mapToObject;
import static pe.plantilla.api.utils.Util.objectToJson;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pe.plantilla.api.model.BasicEntity;
import pe.plantilla.api.repository.IBasicMapper;
import pe.plantilla.api.utils.ApiException;
import pe.plantilla.api.utils.Validador;

import pe.plantilla.api.utils.ValidatorException;
import pe.plantilla.api.utils.dto.GeneralPageTable;

public class BasicService<R extends IBasicMapper<T>,T extends BasicEntity> implements IBasicService<T> {
	
	protected final Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	R repository;
	@Autowired
	Validador<T> validar;
	@Autowired
	IUsuarioService auth;
	
	@Override
	public T getEntity(int id) throws ApiException {
		log.info("Obteniendo entidad con id -> {}", id);
		T entity;
		try {
			 entity = repository.getEntity(id);
			 if(null == entity)
				 throw new ApiException(HttpStatus.NOT_FOUND, "Elemento no encontrado", null);
			 return entity;
		}	catch (ApiException e) {
			log.error("Error api obteniendo entidad con id	{} - {} - {}", id,e.getMessage(), e);
			throw e;
		}	catch (SQLException e) {
			log.error("Error sql al obtener entidad con id {} - {} - {}", id,e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}

	@Override
	public List<T> getAllEntitys() throws ApiException {
		log.info("Listando todos los elementos");
		List<T> entitys;
		try {
			 entitys = repository.getAllEntitys();
			 if(null == entitys)
				 throw new ApiException(HttpStatus.NOT_FOUND, "Elementos no encontrados", null);
			 return entitys;
		}	catch (ApiException e) {
			log.error("Error api obteniendo el listado de elementos {} - {}",e.getMessage(), e);
			throw e;
		}	catch (SQLException e) {
			log.error("Error sql al obtener los elementos {} - {}",e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}

	@Override
	public PageInfo<T> pagingEntitys(Map<String, String> params) throws ApiException {
		log.info("Obteniendo paginación de entitys {} para busqueda {}.",this.getClass(), objectToJson(params));
		try {
			List<T> rptaData = null;
			GeneralPageTable pagData = mapToObject(params, GeneralPageTable.class);
			PageHelper.startPage(pagData.getPage(),pagData.getLimit());
			
			rptaData = repository.pagingEntitys(pagData);
				
			return new PageInfo<T>(rptaData);
		}	catch (ApiException e) {
			log.error("Error api obteniendo el listado de elementos {} - {}",e.getMessage(), e);
			throw e;
		}	catch (SQLException e) {
			log.error("Error sql al obtener los elementos de la paginación {} - {}",e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}	catch (Exception e)	{
			log.error("Error interno al procesar parámetros de la peticion {} - {}",e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}	
	}

	@Override
	public void saveEntity(T entity) throws ApiException {
		log.info("Guardando entidad {}", objectToJson(entity));
		try {
			validar.validarModelo(entity);
			if (validar.isHayErrores())
				throw new ValidatorException("Error de validación", validar.getErrores());
			entity.setFecha_creacion(new Date());
			entity.setCreado_por(auth.getUserToken());
			repository.saveEntity(entity);
		} catch (ValidatorException e) {
			log.error("Errores de validación guardando entidad {} - {} - {}",e.getMessage(), e.getErrores(),e);
			throw new ApiException(HttpStatus.BAD_REQUEST,"Errores de validación en los datos enviados",null,e.getErrores());
		} catch (SQLException e) {
			log.error("Errores sql al guardar entidad {} - {}",e.getMessage(),e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del API, inténtelo más tarde.",e);
		}
	}

	@Override
	public void updateEntity(T entity) throws ApiException {
		log.info("Actualizando entidad {}", objectToJson(entity));
		try {
			getEntity(entity.getId());
			validar.validarModelo(entity);
			if (validar.isHayErrores())
				throw new ValidatorException("Error de validación", validar.getErrores());
			entity.setFecha_modifcacion(new Date());
			entity.setModifcado_por(auth.getUserToken());
			repository.updateEntity(entity);
		}	catch (ApiException e) {
			log.error("No se encontró entidad actualizando {} - {}",e.getMessage(),e);
			throw e;
		}	catch (ValidatorException e) {
			log.error("Errores de validación actualizando entidad {} - {} - {}",e.getMessage(), e.getErrores(),e);
			throw new ApiException(HttpStatus.BAD_REQUEST,"Errores de validación en los datos enviados",null,e.getErrores());
		} 	catch (SQLException e) {
			log.error("Errores sql al guardar entidad {} - {}",e.getMessage(),e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del API, inténtelo más tarde.",e);
		}
	}

	@Override
	public void deleteEntity(int id) throws ApiException {
		log.info("Eliminando entidad {}", id);
		try {
			getEntity(id);
			repository.deleteEntity(id);
		} catch (ApiException e) {
			log.error("No se encontró entidad al eliminar {} - {}",e.getMessage(), e);
			throw e;
		} catch (SQLException e) {
			log.error("Errores sql al eliminar entidad {} - {}",e.getMessage(),e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del API, inténtelo más tarde.",e);
		}
	}
	
	@Override
	public Object searchEntity(Map<String, String> params) throws ApiException {
		Object rpta = null;
		try {
			if(!params.isEmpty()) {
				int id = Integer.parseInt(params.get("id"));
				rpta = getEntity(id);
			}else
				rpta = getAllEntitys();
			
		}	catch (ApiException e) {
			throw e;
		}
		return rpta;
	}

}
