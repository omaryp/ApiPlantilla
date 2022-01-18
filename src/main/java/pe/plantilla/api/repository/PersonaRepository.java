package pe.plantilla.api.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import pe.plantilla.api.model.Persona;

@Mapper
@Repository
public interface PersonaRepository extends IBasicMapper<Persona> {


}
