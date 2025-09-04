package br.com.ocauamotta.PetLar.controller;

import br.com.ocauamotta.PetLar.controller.generic.GenericController;
import br.com.ocauamotta.PetLar.service.CatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/cats")
public class CatController extends GenericController<CatService> {
    public CatController(CatService service) {
        super(service);
    }
}
