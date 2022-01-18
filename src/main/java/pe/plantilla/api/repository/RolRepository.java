package pe.plantilla.api.repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import pe.plantilla.api.model.Rol;

@Mapper
@Repository
public interface RolRepository extends IBasicMapper<Rol> {
	
	List<Rol> getRolesByPerfil(int perfil) throws SQLException,SQLIntegrityConstraintViolationException;
	
	List<Rol> getRolesByUsuario(String usuario ) throws SQLException,SQLIntegrityConstraintViolationException;
	
	List<Rol> getRolesByUsuarioModulo(String usuario,int modulo) throws SQLException,SQLIntegrityConstraintViolationException;

}
