package br.com.ocauamotta.PetLar.controller;

import br.com.ocauamotta.PetLar.controller.generic.GenericController;
import br.com.ocauamotta.PetLar.service.OtherService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api.prefix}/others")
public class OtherController extends GenericController<OtherService> {
    protected OtherController(OtherService service) {
        super(service);
    }
}
