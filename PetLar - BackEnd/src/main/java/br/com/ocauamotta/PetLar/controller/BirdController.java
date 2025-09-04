package br.com.ocauamotta.PetLar.controller;

import br.com.ocauamotta.PetLar.controller.generic.GenericController;
import br.com.ocauamotta.PetLar.service.BirdService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/birds")
public class BirdController extends GenericController<BirdService> {
    protected BirdController(BirdService service) {
        super(service);
    }
}
