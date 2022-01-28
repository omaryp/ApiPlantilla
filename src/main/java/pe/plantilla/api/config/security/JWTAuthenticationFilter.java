package pe.plantilla.api.config.security;

import static pe.plantilla.api.utils.Constants.HEADER_AUTHORIZACION_KEY;
import static pe.plantilla.api.utils.Constants.TOKEN_BEARER_PREFIX;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.stereotype.Component;
import pe.plantilla.api.model.Usuario;
import pe.plantilla.api.model.dto.UsuarioDto;
import static pe.plantilla.api.utils.Util.respuestaApi;

import pe.plantilla.api.service.IJwtService;
import pe.plantilla.api.service.IUsuarioService;
import pe.plantilla.api.utils.dto.DatosSession;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private IUsuarioService userService;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private IJwtService jwtService;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		Usuario credenciales;
		try {
			credenciales = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			return authManager.authenticate(new UsernamePasswordAuthenticationToken(credenciales.getUsuario(),
					credenciales.getPassword()));
		} catch (Exception e) {
			throw new AuthenticationServiceException("Credenciales inválidas");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		DatosSession session;
		String token;
		UsuarioDto user;
		try {
			session = new DatosSession();
			if (auth.getAuthorities().isEmpty()) {
				respuestaApi(null, "Usuario no autorizado, no tiene roles.",HttpStatus.UNAUTHORIZED, response);
			} else {
				user = userService.quitarAuth((UsuarioDto) auth.getPrincipal());
				token = jwtService.crearToken(user.getUsername());
				response.setContentType("application/json");
				response.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + token);

				session.setUser(user);
				session.setToken(token);
				respuestaApi(session, "Transacción OK", HttpStatus.OK, response);
			}
		} catch (Exception e) {
			respuestaApi(null, "Ocurrió un error interno en la aplicación", HttpStatus.INTERNAL_SERVER_ERROR, response);
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		respuestaApi(null, "Usuario no autorizado : " + failed.getMessage(),HttpStatus.UNAUTHORIZED, response);
	}

	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authManager) {
		super.setAuthenticationManager(authManager);
	}
}
