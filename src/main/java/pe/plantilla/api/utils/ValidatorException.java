package pe.plantilla.api.utils;

import java.util.List;

public class ValidatorException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private List<String> errores;
	
	public ValidatorException(String message, List<String> errores) {
		super(message);
		this.message=message;
		this.errores = errores;
	}
	
	public ValidatorException(String message) {
		super(message);
		this.message=message;
		this.errores = null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrores() {
		return errores;
	}

	public void setErrores(List<String> errores) {
		this.errores = errores;
	}
	
	
}
