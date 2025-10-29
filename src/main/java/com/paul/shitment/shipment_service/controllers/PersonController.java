package com.paul.shitment.shipment_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.person.PersonResponseDto;
import com.paul.shitment.shipment_service.services.PersonService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<PersonResponseDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/paged")
    public ResponseEntity<PageResponse<PersonResponseDto>> getAllPersonsPaged(int pageNo, int size, String sortBy) {
        return ResponseEntity.ok(personService.getAllPersonsPaged(pageNo, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDto> getPerson(@PathVariable UUID id) {
        return ResponseEntity.ok(personService.getPerson(id));
    }

    @GetMapping("/ci/{ci}")
    public ResponseEntity<PersonResponseDto> getPersonByCI(@PathVariable String ci) {
        return ResponseEntity.ok(personService.getPersonByCI(ci));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<Boolean> existsByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(personService.existsByPhone(phone));
    }

    @PostMapping
    public ResponseEntity<PersonResponseDto> createPerson(@Valid @RequestBody PersonRequestDto personDto) {
        PersonResponseDto created = personService.createPerson(personDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDto> updatePerson(
            @Valid @RequestBody PersonRequestDto personDto,
            @PathVariable UUID id) {
        PersonResponseDto person = personService.updatePerson(id, personDto);
        return ResponseEntity.ok(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PersonResponseDto> deactivaPerson(@PathVariable UUID id) {
        return ResponseEntity.ok(personService.deletePerson(id));
    }
}
