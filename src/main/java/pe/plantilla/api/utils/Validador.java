package pe.plantilla.api.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class Validador<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(Validador.class);
	
	private boolean noValido;
	
	private List<String> errores;
	
	public void validarModelo(T entrada)  {
		logger.info("Se reciben los datos a validar {} ",Util.objectToJson(entrada));
		errores = new ArrayList<String>();
		noValido = false;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(entrada);
		for (ConstraintViolation<T> violation : violations) {
			errores.add(violation.getMessage());					
		}
		if(errores.size() != 0)
			noValido = true;
		logger.info("Se terminó de validar datos de trazabilidad{} ",Util.objectToJson(entrada));
	}

	public boolean isHayErrores() {
		return noValido;
	}

	public List<String> getErrores() {
		return errores;
	}

}
