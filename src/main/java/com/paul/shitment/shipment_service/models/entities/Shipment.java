package com.paul.shitment.shipment_service.models.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.paul.shitment.shipment_service.exceptions.validation.ShipmentValidationException;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder()
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 30)
    private String internalCode;

    @Column(unique = true, nullable = false, length = 20)
    private String trackingCode;

    @Column(nullable = false)
    private String itemDescription;

    @PositiveOrZero
    private BigDecimal shippingCost;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @ManyToOne
    @JoinColumn(name = "origin_office_id")
    private Office originOffice;

    @ManyToOne
    @JoinColumn(name = "destination_office_id")
    private Office destinationOffice;

    @ManyToOne
    @JoinColumn(name = "createdBy_user_id")
    private AppUser createdBy;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShipmentParty> parties = new HashSet<>();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        if (this.status == ShipmentStatus.DELIVERED) {
            throw new ShipmentValidationException("El envío ya fue entregado");
        }
        this.status = ShipmentStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == ShipmentStatus.DELIVERED) {
            throw new ShipmentValidationException("No se puede cancelar un envío entregado");
        }
        if (this.status == ShipmentStatus.CANCELED) {
            throw new ShipmentValidationException("El envío ya fue cancelado");
        }
        this.status = ShipmentStatus.CANCELED;
    }

    public void addParty(ShipmentParty party) {
        if (this.parties == null)
            this.parties = new HashSet<>();
        
        this.parties.add(party);
        party.setShipment(this);
    }

    /*
     * public void updateFromShipmentUpdateRequestDto(ShipmentUpdateRequestDto
     * shipmentDto) {
     * if (!shipmentDto.senderName().equals(this.getSender().getName()))
     * this.getSender().setName(shipmentDto.senderName());
     * 
     * if (!shipmentDto.senderCI().equals(this.getSender().getCi()) &&
     * !shipmentDto.senderCI().isEmpty())
     * this.getSender().setCi(shipmentDto.senderCI());
     * 
     * if (!shipmentDto.senderPhone().equals(this.getSender().getPhone()))
     * this.getSender().setPhone(shipmentDto.senderPhone());
     * 
     * if (!shipmentDto.recipientName().equals(this.getRecipient().getName()))
     * this.getRecipient().setName(shipmentDto.recipientName());
     * 
     * if (!shipmentDto.recipientCI().equals(this.getRecipient().getCi()) &&
     * !shipmentDto.recipientCI().isEmpty())
     * this.getRecipient().setCi(shipmentDto.recipientCI());
     * 
     * if (!shipmentDto.recipientPhone().equals(this.getRecipient().getPhone()))
     * this.getRecipient().setPhone(shipmentDto.recipientPhone());
     * 
     * if (!this.getItemDescription().equals(shipmentDto.itemDescription()))
     * this.setItemDescription(shipmentDto.itemDescription());
     * 
     * if (this.getShippingCost() != shipmentDto.shippingCost()) {
     * this.setShippingCost(shipmentDto.shippingCost());
     * }
     * }
     */

}
