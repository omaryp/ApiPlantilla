package pe.plantilla.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.plantilla.api.model.Persona;
import pe.plantilla.api.utils.dto.RespuestaApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author Omar Yarleque
 */
public class Util {

	private static final Logger logger = LoggerFactory.getLogger(Util.class);

	public static String objectToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException ex) {
			logger.error(ex.toString());
		}
		return json;
	}

	public static void writeBytesToFileApache(String fileOutput, byte[] bytes) throws IOException {

		FileUtils.writeByteArrayToFile(new File(fileOutput), bytes);

	}
	
	public static boolean isDateEquals(Date date1,Date date2) throws IOException {
		SimpleDateFormat cDate1 = new SimpleDateFormat("yyyy-MM-dd");
        String ccDate1 = cDate1.format(date1);

        SimpleDateFormat cDate2 = new SimpleDateFormat("yyyy-MM-dd");
        String ccDate2 = cDate2.format(date2);
		
        return ccDate1.equals(ccDate2);

	}
	
	 public static String encodeFileToBase64Binary(File file)throws ApiException{
         String encodedString = null;
         try {
        	 byte[] fileContent = FileUtils.readFileToByteArray(file);
             encodedString = Base64.getEncoder().encodeToString(fileContent);
         } catch (FileNotFoundException e) {
             throw new ApiException("No se encontró archivo", e);
         } catch (IOException e) {
        	 throw new ApiException("Error de entrada y salida", e);
         }
         return encodedString;
     }

	public static <T> T jsonToObject(String jsonString, Class<T> clazz) {
		T obj = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			obj = mapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return obj;
	}

	public static <T> T mapToObject(Map<String, String> aMap, Class<T> t) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(aMap, mapper.getTypeFactory().constructType(t));
		} catch (Exception e) {
			throw e;
		}
	}

	public static <T> ResponseEntity<RespuestaApi<T>> respuestaApi(T contenido, String mensaje, HttpStatus status) {
		RespuestaApi<T> response = new RespuestaApi<T>();
		response.setMessage(mensaje);
		response.setContenido(contenido);
		return new ResponseEntity<RespuestaApi<T>>(response, status);
	}

	public static <T> void respuestaApi(T contenido, String mensaje,  HttpStatus status, HttpServletResponse response) {
		try {
			PrintWriter writer = response.getWriter();
			response.setContentType("application/json");
			RespuestaApi<T> rpta = new RespuestaApi<T>();
			rpta.setMessage(mensaje);
			rpta.setContenido(contenido);
			response.setStatus(status.value());
			writer.println(objectToJson(rpta));
		} catch (Exception e) {
			logger.error("Error al enviar rpta autenticación {} - {}", e.getMessage(), e);
		}
	}

	public static <T extends Persona> Persona getPersona(T entity) {
		Persona persona = new Persona();
		persona.setId(entity.getId());
		persona.setApellidos(entity.getApellidos());
		persona.setCorreo(entity.getCorreo());
		persona.setCreado_por(entity.getCreado_por());
		persona.setDireccion(entity.getDireccion());
		persona.setDni(entity.getDni());
		persona.setEliminado_por(entity.getEliminado_por());
		persona.setEstado(entity.getEstado());
		persona.setFecha_creacion(entity.getFecha_creacion());
		persona.setFecha_modifcacion(entity.getFecha_modifcacion());
		persona.setFecha_nacimiento(entity.getFecha_nacimiento());
		persona.setModifcado_por(entity.getModifcado_por());
		persona.setNombres(entity.getNombres());
		persona.setTelefono(entity.getTelefono());
		return persona;
	}
	
	public static String formatearFecha(String format,Date fecha) {
		SimpleDateFormat objSDF = new SimpleDateFormat(format);
		return objSDF.format(fecha);
	}

}
