package pe.plantilla.api.repository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import pe.plantilla.api.model.Modulo;

@Mapper
@Repository
public interface ModuloRepository extends IBasicMapper<Modulo> {

	List<Modulo> getModulosByUsuario(String usuario) throws SQLException,SQLIntegrityConstraintViolationException;
	List<Modulo> getAllModulos() throws SQLException,SQLIntegrityConstraintViolationException;
}
