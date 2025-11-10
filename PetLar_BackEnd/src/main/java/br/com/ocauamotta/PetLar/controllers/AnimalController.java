package br.com.ocauamotta.PetLar.controllers;

import br.com.ocauamotta.PetLar.dtos.AnimalRequestDto;
import br.com.ocauamotta.PetLar.dtos.AnimalResponseDto;
import br.com.ocauamotta.PetLar.services.AnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${api.prefix}/animals")
public class AnimalController {

    @Autowired
    private AnimalService service;

    @GetMapping
    public ResponseEntity<Page<AnimalResponseDto>> findAll(Pageable pageable,
                                                           @RequestParam(required = false, defaultValue = "disponivel") String status,
                                                           @RequestParam(required = false) String type) {
        return ResponseEntity.ok(service.findAll(pageable, status, type));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<AnimalResponseDto> findById(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<AnimalResponseDto> save(@RequestBody @Valid AnimalRequestDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<AnimalResponseDto> update(@PathVariable(value = "id") String id, @RequestBody AnimalRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
