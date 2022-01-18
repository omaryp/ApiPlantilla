package pe.plantilla.api.utils.dto;


public class RespuestaApi<T> {
	
	private String message;
	private T contenido;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getContenido() {
		return contenido;
	}
	public void setContenido(T contenido) {
		this.contenido = contenido;
	}
	
}
