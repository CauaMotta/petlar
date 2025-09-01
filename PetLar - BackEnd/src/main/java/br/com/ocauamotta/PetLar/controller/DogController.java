package br.com.ocauamotta.PetLar.controller;

import br.com.ocauamotta.PetLar.controller.generic.GenericController;
import br.com.ocauamotta.PetLar.service.DogService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/dogs")
public class DogController extends GenericController<DogService> {
    public DogController(DogService service) {
       super(service);
    }
}
