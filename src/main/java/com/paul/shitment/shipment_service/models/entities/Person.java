package com.paul.shitment.shipment_service.models.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.models.enums.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(unique = true)
    private String documentNumber;

    private String fullName;
    
    @Column(unique = true)
    private String phone;

    private boolean registered;

    private boolean active = true;

    @OneToMany(mappedBy = "person")
    private Set<ShipmentParty> shipmentParty = new HashSet<>();

    @OneToOne(mappedBy = "person")
    private AppUser user;

    public void deactivate() {
        if(active == true)
            active = false;
    }

    public void updateFromRequestDto(PersonRequestDto personDto) {
        if (!this.getFullName().equals(personDto.fullName()))
            this.setFullName(personDto.fullName());

        if (!this.getDocumentNumber().equals(personDto.documentNumber()))
            this.setDocumentNumber(personDto.documentNumber());

        if (!personDto.phone().equals(this.getPhone()))
            this.setPhone(personDto.phone());
    }
}
