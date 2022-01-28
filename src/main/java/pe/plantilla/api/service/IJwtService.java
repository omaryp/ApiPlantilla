package pe.plantilla.api.service;
import pe.plantilla.api.utils.ApiException;

import javax.servlet.http.HttpServletRequest;

public interface IJwtService {

    String verificarToken(HttpServletRequest req) throws ApiException;

    boolean existeJWTToken(HttpServletRequest request) ;

    String crearToken(String user) throws ApiException;
}
