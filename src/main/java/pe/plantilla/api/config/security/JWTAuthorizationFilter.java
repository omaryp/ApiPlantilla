package pe.plantilla.api.config.security;

import static pe.plantilla.api.utils.Constants.HEADER_AUTHORIZACION_KEY;
import static pe.plantilla.api.utils.Constants.SUPER_SECRET_KEY;
import static pe.plantilla.api.utils.Constants.TOKEN_BEARER_PREFIX;
import static pe.plantilla.api.utils.Util.respuestaApi;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import pe.plantilla.api.service.IRolService;
import pe.plantilla.api.utils.ApiException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private IRolService rolService;
	
	public JWTAuthorizationFilter(AuthenticationManager authManager,IRolService rolService) {
		super(authManager);
		this.rolService = rolService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		try {
			String header = req.getHeader(HEADER_AUTHORIZACION_KEY);
			if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX)) {
				SecurityContextHolder.clearContext();
				chain.doFilter(req, res);
				return;
			}
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | ApiException e) {
			respuestaApi("Error en el token, expiró ó no es válido", e.getMessage(), HttpStatus.FORBIDDEN, res);
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws ApiException {
		List<String> authorities = null;
		List<GrantedAuthority> auths = null;
		Claims claims = null;
		String user = "";
		String tokenHeader = "";
		try {
			tokenHeader = request.getHeader(HEADER_AUTHORIZACION_KEY);
			if (null != tokenHeader) {
				
				// Se procesa el token y se recupera el usuario.
				claims = Jwts.parser()
							.setSigningKey(SUPER_SECRET_KEY.getBytes())
							.parseClaimsJws(tokenHeader.replace(TOKEN_BEARER_PREFIX, ""))
							.getBody();
				
				user = claims.getSubject();
				authorities = rolService.getAuthorities(user);
				
				if (null != user) {
					if(null != authorities) {
						auths = authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
						return new UsernamePasswordAuthenticationToken(claims.getSubject(), null,auths);
					}
				}
				return null;
			}
			return null;
		} catch (ApiException e) {
			throw e;
		}
	}
}