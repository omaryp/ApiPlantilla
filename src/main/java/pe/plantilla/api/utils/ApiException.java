package pe.plantilla.api.utils;

import java.util.List;

import org.springframework.http.HttpStatus;


public class ApiException extends Exception {
	
	private HttpStatus status;
	private List<String> errores;
	
	private static final long serialVersionUID = -5272256098423403364L;
	
	public ApiException(HttpStatus status,String message,Throwable ex,List<String> errores) {
		super(message,ex);
		this.status = status;
		this.errores = errores;
	}
	
	public ApiException(HttpStatus status,String message,Throwable ex) {
		super(message,ex);
		this.status = status;
	}
	
	public ApiException(HttpStatus status,String message) {
		super(message);
		this.status = status;
	}
	
	public ApiException(String message,Throwable ex) {
		super(message,ex);
	}
	
	public ApiException(String message) {
		super(message);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public List<String> getErrores() {
		return errores;
	}
	
}
