package pe.plantilla.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import pe.plantilla.api.model.Rol;
import pe.plantilla.api.service.IRolService;

@RestController
@RequestMapping("/rol")
public class RolApi extends BasicController<Rol, IRolService> {
	
}