package br.com.ocauamotta.PetLar.controller.generic;

import br.com.ocauamotta.PetLar.dto.CreateAnimalDTO;
import br.com.ocauamotta.PetLar.dto.AnimalDTO;
import br.com.ocauamotta.PetLar.service.generic.IAnimalCrudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
public abstract class GenericController<S extends IAnimalCrudService> {

    protected final S service;

    protected GenericController(S service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<AnimalDTO>> findAll(Pageable pageable, @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.findAll(pageable, status));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AnimalDTO> findById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AnimalDTO> register(@RequestBody @Valid CreateAnimalDTO createAnimalDTO) {
        return ResponseEntity.ok(service.save(createAnimalDTO));
    }

    @PutMapping
    public ResponseEntity<AnimalDTO> update(@RequestBody @Valid AnimalDTO animalDTO) {
        return ResponseEntity.ok(service.update(animalDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable(value = "id") String id) {
        service.delete(id);
        return ResponseEntity.ok("Removido com sucesso.");
    }
}
