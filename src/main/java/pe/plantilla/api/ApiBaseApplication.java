package pe.plantilla.api;

import java.io.File;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import pe.plantilla.api.config.ParametrosApi;

@SpringBootApplication
@ComponentScan({"pe.plantilla.api"})
//@PropertySource("file:${user.home}/apibase/config/application.properties")
@PropertySource("classpath:application.properties")
public class ApiBaseApplication {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ParametrosApi params;
	
	public static void main(String[] args) {
		SpringApplication.run(ApiBaseApplication.class, args);
	}
	
	@PostConstruct
	public void loadLogger() {
		LoggerContext context = (LoggerContext) LogManager.getContext();
		File file = null;
		file = new File(params.getRutaConfigLog4j2());
		
		context.setConfigLocation(file.toURI());
		log.info("Log4j2 cargado desde el archivo : " + file.getAbsolutePath());
	}

}
