package com.paul.shitment.shipment_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paul.shitment.shipment_service.repositories.Data;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/data")
@AllArgsConstructor
public class DataController {

    private final Data data;

    @GetMapping("/create")
    public ResponseEntity<String> createData() {
        data.createData();
        return ResponseEntity.ok("Se creo exitosamente, office, person, user");
    }

}
