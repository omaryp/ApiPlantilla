package pe.plantilla.api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.stereotype.Service;
import pe.plantilla.api.utils.ApiException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static pe.plantilla.api.utils.Constants.HEADER_AUTHORIZACION_KEY;
import static pe.plantilla.api.utils.Constants.SUPER_SECRET_KEY;
import static pe.plantilla.api.utils.Constants.ISSUER_INFO;
import static pe.plantilla.api.utils.Constants.TOKEN_BEARER_PREFIX;
import static pe.plantilla.api.utils.Constants.TOKEN_EXPIRATION_TIME;


@Service
public class JwtService implements IJwtService {

    @Override
    public String verificarToken(HttpServletRequest request) throws ApiException {
        JWTVerifier verifier;
        String tokenHeader;
        try {
            tokenHeader = request.getHeader(HEADER_AUTHORIZACION_KEY);
            verifier = JWT
                    .require(Algorithm.HMAC512(SUPER_SECRET_KEY.getBytes()))
                    .withIssuer(ISSUER_INFO)
                    .build();
            DecodedJWT jwt = verifier.verify(tokenHeader.replace(TOKEN_BEARER_PREFIX, ""));
            return jwt.getSubject();
        }catch (JWTVerificationException e){
            throw new ApiException("Token no válido",e);
        }
    }

    @Override
    public boolean existeJWTToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER_AUTHORIZACION_KEY);
        if (authenticationHeader == null || !authenticationHeader.startsWith(TOKEN_BEARER_PREFIX))
            return false;
        return true;
    }

    @Override
    public String crearToken(String user) throws ApiException {
        try {
            return JWT.create()
                    .withClaim("typ","JWT")
                    .withIssuer(ISSUER_INFO)
                    .withSubject(user)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                    .sign(Algorithm.HMAC512(SUPER_SECRET_KEY.getBytes()));
        }catch (JWTCreationException e){
            throw new ApiException("Error al crear el token",e);
        }
    }
}
