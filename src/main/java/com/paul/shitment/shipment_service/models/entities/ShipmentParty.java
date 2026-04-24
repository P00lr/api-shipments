package com.paul.shitment.shipment_service.models.entities;

import java.util.UUID;

import com.paul.shitment.shipment_service.models.enums.DocumentType;
import com.paul.shitment.shipment_service.models.enums.ShipmentPartyRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "shipment_party")
public class ShipmentParty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private String documentNumber;

    private String fullName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private ShipmentPartyRole role;

    private String note;

    @ManyToOne
    private Shipment shipment;

    @ManyToOne(optional = true)
    @JoinColumn(name = "person_id", nullable = true)
    private Person person;
}
