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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;
    
    @Column(unique = true)
    private String email;
    
    private boolean active;

    @OneToOne
    @JoinColumn(name = "person_id", unique = true)
    private Person person;

    @JsonManagedReference
    @OneToMany(mappedBy = "createdBy")
    private List<Shipment> shipmentsCreated = new ArrayList<>();

    public AppUser(String username, String password, String email, Person person) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.person = person;
        active = true;
    }

}
