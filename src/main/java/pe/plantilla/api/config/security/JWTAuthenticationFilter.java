package pe.plantilla.api.config.security;

import static pe.plantilla.api.utils.Constants.HEADER_AUTHORIZACION_KEY;
import static pe.plantilla.api.utils.Constants.ISSUER_INFO;
import static pe.plantilla.api.utils.Constants.SUPER_SECRET_KEY;
import static pe.plantilla.api.utils.Constants.TOKEN_BEARER_PREFIX;
import static pe.plantilla.api.utils.Constants.TOKEN_EXPIRATION_TIME;

import static pe.plantilla.api.utils.Constants.SECRET_PASSWORD;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pe.plantilla.api.model.Rol;
import pe.plantilla.api.model.Usuario;
import pe.plantilla.api.model.dto.UsuarioDto;
import static pe.plantilla.api.utils.Util.respuestaApi;

import pe.plantilla.api.utils.dto.Jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private Usuario credenciales;
	private Authentication auth;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			credenciales = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credenciales.getUsuario(),
					credenciales.getPassword(), new ArrayList<>()));
			return auth;
		} catch (Exception e) {
			throw new AuthenticationServiceException("Credenciales inválidas");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		try {
			Jwt jwt = new Jwt();
			String token;
			UsuarioDto user;
			if (auth.getAuthorities().isEmpty()) {
				respuestaApi(null, "Usuario no autorizado, no tiene roles.",HttpStatus.UNAUTHORIZED, response);
			} else {
				user = (UsuarioDto) auth.getPrincipal();
				user = cargaNuevoUsuario(user);
				token = Jwts.builder().setHeaderParam("typ", "JWT").setIssuer(ISSUER_INFO)
						.setSubject(user.getUsername())
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
						.signWith(SignatureAlgorithm.HS512, SUPER_SECRET_KEY.getBytes()).compact();

				response.setContentType("application/json");
				response.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + " " + token);

				jwt.setUser(user);
				jwt.setToken(token);
				respuestaApi(jwt, "Transacción OK", HttpStatus.OK, response);
			}
			
		} catch (Exception e) {
			respuestaApi(null, "Ocurrió un error interno en la aplicación", HttpStatus.INTERNAL_SERVER_ERROR, response);
		}
	}

	private UsuarioDto cargaNuevoUsuario(UsuarioDto oldDto) {
		UsuarioDto nvoDto = null;
		nvoDto = new UsuarioDto(oldDto.getUsername(), SECRET_PASSWORD, oldDto.isEnabled(),
					true, true, true,new ArrayList<Rol>());
		nvoDto.setId(oldDto.getId());
		nvoDto.setNombres(oldDto.getNombres());
		nvoDto.setApellidos(oldDto.getApellidos());
		nvoDto.setCorreo(oldDto.getCorreo());
		nvoDto.setPerfil(oldDto.getPerfil());
		nvoDto.setNombrePerfil(oldDto.getNombrePerfil());
		nvoDto.setModulos(oldDto.getModulos());
		return nvoDto;
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		respuestaApi(null, "Usuario no autorizado : " + failed.getMessage(),HttpStatus.UNAUTHORIZED, response);

	}

}
