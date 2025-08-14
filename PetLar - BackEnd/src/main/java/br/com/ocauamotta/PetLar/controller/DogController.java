package br.com.ocauamotta.PetLar.controller;

import br.com.ocauamotta.PetLar.dto.CreateDogDTO;
import br.com.ocauamotta.PetLar.dto.DogDTO;
import br.com.ocauamotta.PetLar.service.DogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/dog")
public class DogController {

    private DogService service;

    public DogController(DogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<DogDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DogDTO> findById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<DogDTO> register(@RequestBody @Valid CreateDogDTO createDogDTO) {
        return ResponseEntity.ok(service.save(createDogDTO));
    }

    @PutMapping
    public ResponseEntity<DogDTO> update(@RequestBody @Valid DogDTO dogDTO) {
        return ResponseEntity.ok(service.update(dogDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") String id) {
        service.delete(id);
        return ResponseEntity.ok("Removido com sucesso.");
    }
}
