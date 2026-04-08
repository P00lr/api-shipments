package com.paul.shitment.shipment_service.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;

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
    @NonNull
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String ci;

    @Column(unique = true)
    private String phone;

    private boolean registered;

    private boolean active = true;

    @JsonManagedReference
    @OneToMany(mappedBy = "sender")
    private List<Shipment> senderShipments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "recipient")
    private List<Shipment> recipientShipments = new ArrayList<>();

    @JsonManagedReference
    @OneToOne(mappedBy = "person")
    private AppUser user;

    public void deactivate() {
        if(active == true)
            active = false;
    }

    public void updateFromRequestDto(PersonRequestDto personDto) {
        if (!this.getName().equals(personDto.name()))
            this.setName(personDto.name());

        if (!this.getCi().equals(personDto.ci()))
            this.setCi(personDto.ci());

        if (!personDto.phone().equals(this.getPhone()))
            this.setPhone(personDto.phone());
    }


    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", ci=" + ci + ", phone=" + phone + "]";
    }

}
