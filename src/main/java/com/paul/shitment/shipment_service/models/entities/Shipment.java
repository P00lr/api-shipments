package com.paul.shitment.shipment_service.models.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.paul.shitment.shipment_service.dto.shipment.ShipmentUpdateRequestDto;
import com.paul.shitment.shipment_service.exceptions.validation.ShipmentValidationException;
import com.paul.shitment.shipment_service.models.enums.ShipmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder()
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String itemDescription;

    private Double shippingCost;

    @Column(unique = true, nullable = false, length = 30)
    private String internalCode;

    @Column(unique = true, nullable = false, length = 20)
    private String trackingCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deliveredAt;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @JsonBackReference  
    @ManyToOne
    @JoinColumn(name = "origin_office_id")
    private Office originOffice;

    @JsonBackReference  
    @ManyToOne
    @JoinColumn(name = "destination_office_id")
    private Office destinationOffice;

    @JsonBackReference  
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Person sender;

    @JsonBackReference  
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Person recipient;

    @JsonBackReference  
    @ManyToOne
    @JoinColumn(name = "createdBy_user_id")
    private AppUser createdBy;

    public Shipment(
            Office originOffice,
            Office destinationOffice,
            Person sender,
            Person recipient,
            AppUser createdBy,
            String itemDescription,
            Double shippingCost,
            String internalCode,
            String trackingCode,
            ShipmentStatus status) {

        this.originOffice = originOffice;
        this.destinationOffice = destinationOffice;
        this.sender = sender;
        this.recipient = recipient;
        this.createdBy = createdBy;
        this.itemDescription = itemDescription;
        this.shippingCost = shippingCost;
        this.internalCode = internalCode;
        this.trackingCode = trackingCode;
        this.status = status;
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
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

    public void updateFromShipmentUpdateRequestDto(ShipmentUpdateRequestDto shipmentDto) {
        if (!shipmentDto.senderName().equals(this.getSender().getName()))
            this.getSender().setName(shipmentDto.senderName());

        if (!shipmentDto.senderCI().equals(this.getSender().getCi()) && !shipmentDto.senderCI().isEmpty())
            this.getSender().setCi(shipmentDto.senderCI());

        if (!shipmentDto.senderPhone().equals(this.getSender().getPhone()))
            this.getSender().setPhone(shipmentDto.senderPhone());

        if (!shipmentDto.recipientName().equals(this.getRecipient().getName()))
            this.getRecipient().setName(shipmentDto.recipientName());

        if (!shipmentDto.recipientCI().equals(this.getRecipient().getCi()) && !shipmentDto.recipientCI().isEmpty())
            this.getRecipient().setCi(shipmentDto.recipientCI());

        if (!shipmentDto.recipientPhone().equals(this.getRecipient().getPhone()))
            this.getRecipient().setPhone(shipmentDto.recipientPhone());

        if (!this.getItemDescription().equals(shipmentDto.itemDescription()))
            this.setItemDescription(shipmentDto.itemDescription());

        if (this.getShippingCost() != shipmentDto.shippingCost()) {
            this.setShippingCost(shipmentDto.shippingCost());
        }
    }

}
