package pe.plantilla.api.repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import pe.plantilla.api.model.Usuario;
import pe.plantilla.api.model.dto.UsuarioSessionDto;

@Mapper
@Repository
public interface UsuarioRepository extends IBasicMapper<Usuario> {
	
	Usuario findByUsername(String username) throws SQLException,SQLIntegrityConstraintViolationException;
	
	void disabledUserbyUsername(String username) throws SQLException,SQLIntegrityConstraintViolationException;
	
	void disabledUserbyCodigo(int idUsuario ) throws SQLException,SQLIntegrityConstraintViolationException;

	void actualizarUserByUsername(Usuario user) throws SQLException,SQLIntegrityConstraintViolationException;

	UsuarioSessionDto getDatosSession(int id) throws SQLException,SQLIntegrityConstraintViolationException;
	
	void actualizarUsuario(Usuario datos) throws SQLException,SQLIntegrityConstraintViolationException;
}
