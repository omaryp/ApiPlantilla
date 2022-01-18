package pe.plantilla.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.plantilla.api.model.Modulo;
import pe.plantilla.api.service.IModuloService;

@RestController
@RequestMapping("/modulo")
public class ModuloApi extends BasicController<Modulo, IModuloService> {

}