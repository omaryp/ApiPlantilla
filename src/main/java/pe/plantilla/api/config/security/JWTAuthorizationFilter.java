package pe.plantilla.api.config.security;

import static pe.plantilla.api.utils.Util.respuestaApi;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.stereotype.Component;
import pe.plantilla.api.service.IJwtService;
import pe.plantilla.api.service.IRolService;
import pe.plantilla.api.utils.ApiException;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private AuthenticationManager authManager;
	@Autowired
	private IJwtService jwt;
	@Autowired
	private IRolService rolService;
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		try {
			if(!jwt.existeJWTToken(req)){
				SecurityContextHolder.clearContext();
				return ;
			}
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (ApiException e) {
			respuestaApi("Error en el token, expiró ó no es válido", e.getMessage(), HttpStatus.FORBIDDEN, res);
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws ApiException {
		List<String> authorities;
		List<GrantedAuthority> auths;
		String user;
		try {
			user = jwt.verificarToken(request);
			authorities = rolService.getAuthorities(user);
			if(null != authorities) {
				auths = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
				return new UsernamePasswordAuthenticationToken(user, null,auths);
			}
			return null;
		} catch (ApiException e) {
			throw e;
		}
	}
}