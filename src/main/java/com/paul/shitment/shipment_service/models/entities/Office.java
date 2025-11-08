package com.paul.shitment.shipment_service.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "office")
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String address;

    @Column(unique = true)
    private String phone;

    private boolean active;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "originOffice")
    private List<Shipment> originShipments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "destinationOffice")
    private List<Shipment> destinationShipments = new ArrayList<>();


    public Office(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.active = true;
    }
}
