package pe.plantilla.api.utils;

public class Constants {

	// Spring Security
	public static final String LOGIN_URL = "/login";
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
	public static final String TOKEN_BEARER_PREFIX = "Bearer ";
	public static final String SECRET_PASSWORD = "..::$3CR3T_P4SS::..";

	// JWT
	public static final String ISSUER_INFO = "tuapi.com";
	public static final String SUPER_SECRET_KEY = "@ITHg%JaxPw0Z#g&@7#$&KpkIcq48sdfjio73458912n4cqñspo2940asYu#6XUjy#RH@^nPFyF3f%%0DcY";
	public static final long TOKEN_EXPIRATION_TIME = 86400000; //24 horas
			
	public final static int ACTIVO = 1; 
	public final static int DESACTIVADO = 0;
	
	public static final String RESOURCE_CAPTCHA = "/captcha";
}
