package pe.plantilla.api.service;

import static java.util.Collections.emptyList;
import static pe.plantilla.api.utils.Constants.SECRET_PASSWORD;
import static pe.plantilla.api.utils.Util.getPersona;
import static pe.plantilla.api.utils.Util.mapToObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pe.plantilla.api.model.Modulo;
import pe.plantilla.api.model.Perfil;
import pe.plantilla.api.model.Persona;
import pe.plantilla.api.model.Rol;
import pe.plantilla.api.model.Usuario;
import pe.plantilla.api.model.dto.PermisoDto;
import pe.plantilla.api.model.dto.UsuarioDto;
import pe.plantilla.api.model.dto.UsuarioSessionDto;
import pe.plantilla.api.repository.ModuloRepository;
import pe.plantilla.api.repository.PersonaRepository;
import pe.plantilla.api.repository.UsuarioRepository;
import pe.plantilla.api.utils.ApiException;
import pe.plantilla.api.utils.Util;
import pe.plantilla.api.utils.ValidatorException;
import pe.plantilla.api.utils.dto.GeneralPageTable;

@Service
public class UsuarioService extends BasicService<UsuarioRepository,Usuario> implements UserDetailsService, IUsuarioService {

	@Autowired
	private ModuloRepository modRepository;	
	
	@Autowired
	private IRolService rolService;
	@Autowired
	private IPerfilService perfilService;
	@Autowired
	private PersonaRepository perRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user ;
		List<Rol> roles = emptyList();
		UsuarioDto userDto;
		Perfil perfil;

		try {
			user = findByUsername(username);
			if (user == null) {
				throw new UsernameNotFoundException(username + "no existe.");
			}

			log.info("Cargando usuario {}", Util.objectToJson(user));
			roles = rolService.getRolesByUsuario(username);
			perfil = perfilService.getEntity(user.getPerfiles_id());

			userDto = new UsuarioDto(user.getUsuario(), user.getPassword(), user.isActivo(), true, true, true, roles);
			userDto.setId(user.getId());
			userDto.setNombres(user.getNombres());
			userDto.setApellidos(user.getApellidos());
			userDto.setCorreo(user.getCorreo());
			userDto.setPerfil(user.getPerfiles_id());
			userDto.setNombrePerfil(perfil.getNombre());
			userDto.setModulos(getAuthorities(username, roles));
			
		} catch (Exception e) {
			log.error("Error al autenticar {}", e);
			throw new UsernameNotFoundException("Error interno al iniciar session. "+e.getMessage(),e);
		}
		return userDto;
	}
	
	private List<PermisoDto> getAuthorities(String usuario ,List<Rol> roles) throws ApiException{
		final List<PermisoDto> permisos = new ArrayList<PermisoDto>();
		List<Modulo> modUser = null;
		Map<Integer, Modulo> modulos = null;
		
		try {
			modUser = modRepository.getModulosByUsuario(usuario);
			modulos = modUser.stream().collect(Collectors.toMap(Modulo::getId, mod -> mod));
			final Map<Integer, List<Rol>> rolModulo = roles.stream().collect(Collectors.groupingBy(Rol::getModulos_id));
			modulos.forEach((key,modulo) -> {
				PermisoDto permiso = new PermisoDto();
				permiso.setModulo(modulo);
				permiso.setRoles(rolModulo.get(key));
				permisos.add(permiso);
			});
			
		}catch (SQLException e) {
			
		}
		return permisos;
	}
	
	@Override
	public Usuario findByUsername(String username) throws ApiException {
		try {
			Usuario entity = repository.findByUsername(username);
			return entity;
		} catch (SQLException e) {
			log.error("Error sql al buscar usuario por username {} - {} - {}",username,e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}

	@Override
	public void disabledUserbyUsername(String username) throws ApiException {
		try {
			repository.disabledUserbyUsername(username);
		}catch (SQLException e) {
			log.error("Error sql al buscar usuario por username {} - {} - {}",username,e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		}
	}

	@Override
	public PageInfo<Usuario> paginandoUsuario(Map<String, String> params) throws ApiException{
		log.info("Paginando usuarios{}.",params);
		try {
			List<Usuario> rptaData;
			GeneralPageTable pagData = mapToObject(params, GeneralPageTable.class);
			PageHelper.startPage(pagData.getPage(),pagData.getLimit());
			
			rptaData = repository.pagingEntitys(pagData);
			PageInfo<Usuario> rpta  = new PageInfo<Usuario>(rptaData);
			rpta.setList(procesarLista(rpta.getList()));
				
			return rpta;
		} catch (SQLException e) {
			log.error("Error sql al paginar usuarios {} - {}",e.getMessage(), e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		} catch (Exception e) {
			log.error("Error al convertir map a objeto del tipo GeneralPageTable {} - {} ",e.getMessage(),e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del API, inténtelo más tarde.", e);
		} 
	}

	@Override
	public UsuarioDto quitarAuth(UsuarioDto oldDto) {
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

	private List<Usuario> procesarLista(List<Usuario> datos){
		List<Usuario> rpta = new ArrayList<Usuario>();
		datos.forEach((entity) -> {
			entity.setIdUsuario(entity.getId());
			entity.setPassword(SECRET_PASSWORD);
			entity.setPassword2(SECRET_PASSWORD);
			rpta.add(entity);
		});
		return rpta;
	}

	@Override
	public void guardarUsuario(Usuario entity) throws ApiException {
		Persona padre = null;
		try {
			validar.validarModelo(entity);
			if (validar.isHayErrores())
				throw new ValidatorException("Errores de validación al guardar en datos de entrada", validar.getErrores());
			
			entity.setFecha_creacion(new Date());
			entity.setCreado_por(getUserToken());
			entity.setEstado(1);
			padre = getPersona(entity);
			padre.setEstado(1);
			perRepository.saveEntity(padre);
			
			entity.setPersonas_id(padre.getId());
			entity.setUsuario(entity.getUsuario().toUpperCase());
			entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
			entity.setActivo(true);
			repository.saveEntity(entity);
							
		}catch (ValidatorException e) {
			log.error("Errores de validación al intentar guardar usuario", e.getErrores());
			throw new ApiException(HttpStatus.BAD_REQUEST,"Errores de validación en los datos enviados",e,e.getErrores());
		}catch (SQLException e) {
			log.error("Errores sql al guardar usuario {} - {}",e.getMessage(),e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del API, inténtelo más tarde.",e);
		}
	}
	
	@Override
	public String getUserToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}

	@Override
	public void actualizarUsuario(Usuario entity) throws ApiException {
		try {
			validar.validarModelo(entity);
			if (validar.isHayErrores())
				throw new ValidatorException("Errores de validación al actualizar en datos de entrada", validar.getErrores());
			entity.setFecha_modifcacion(new Date());
			entity.setModifcado_por(getUserToken());
			Persona padre = getPersona(entity);
			padre.setId(entity.getPersonas_id());
			perRepository.updateEntity(padre);
			
			if(!entity.getPassword().equals(SECRET_PASSWORD)) {
				entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
				repository.updateEntity(entity);
			}else
				repository.actualizarUsuario(entity);
				
		}catch (ValidatorException e) {
			log.error("Errores de validación al intentar actualizar usuario", e.getErrores());
			throw new ApiException(HttpStatus.BAD_REQUEST,"Errores de validación en los datos enviados",e,e.getErrores());
		}catch (SQLException e) {
			log.error("Errores sql al actualizar usuario {} - {}",e.getMessage(),e);
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Error interno del API, inténtelo más tarde.",e);
		}
	}

	@Override
	public UsuarioSessionDto getDatosSession() throws ApiException {
		try {
			String username = getUserToken();
			Usuario user = findByUsername(username);
			UsuarioSessionDto dto = new UsuarioSessionDto();
			dto.setApellidos(user.getApellidos());
			dto.setCorreo(user.getCorreo());
			dto.setDireccion(user.getDireccion());
			dto.setDni(user.getDni());
			dto.setFecha_nacimiento(user.getFecha_nacimiento());
			dto.setId(user.getId());
			dto.setNombres(user.getNombres());
			dto.setPassword(SECRET_PASSWORD);
			dto.setPersonas_id(user.getPersonas_id());
			dto.setTelefono(user.getTelefono());
			dto.setUsuario(user.getUsuario());
			return dto;
		}catch (ApiException e) {
			log.error("Errores sql al obtener datos de usuario de sessión {} - {}",e.getMessage(),e);
			throw e;
		}
		
	}

}
