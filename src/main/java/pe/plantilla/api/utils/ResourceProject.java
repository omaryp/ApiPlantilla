package pe.plantilla.api.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ResourceProject {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	
	public File getResource(String ruta) throws ApiException {
		try {
			Resource fileResource = resourceLoader.getResource("classpath:"+ruta);
			return fileResource.getFile();
		} catch (Exception e) {
			throw new ApiException("Error al cargar recurso", e);
		}
	}
	
}
