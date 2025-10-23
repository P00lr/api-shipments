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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String ci;

    @Column(unique = true)
    private String phone;

    private boolean registered;

    private boolean active;

    @JsonManagedReference
    @OneToMany(mappedBy = "sender")
    private List<Shipment> senderShipments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "recipient")
    private List<Shipment> recipientShipments = new ArrayList<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "person")
    private AppUser user;


    public Person(String name, String ci, String phone) {
        this.name = name;
        this.ci = ci;
        this.phone = phone;
        this.active = true;
    }


    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", ci=" + ci + ", phone=" + phone + "]";
    }

}
