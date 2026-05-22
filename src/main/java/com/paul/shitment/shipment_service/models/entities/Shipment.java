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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder()
@Getter
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

    @Setter(AccessLevel.PRIVATE)
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
    @Builder.Default
    private Set<ShipmentParty> parties = new HashSet<>();

    @ManyToOne
    private Vehicle vehicle;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void deliver() {
        
        if(status != ShipmentStatus.WAITING_PICKUP)
            throw new ShipmentValidationException("El envio no puede ser entregado al cliente");
        
        if (status == ShipmentStatus.DELIVERED)
            throw new ShipmentValidationException("El envío ya fue entregado");
            
        status = ShipmentStatus.DELIVERED;
        deliveredAt = LocalDateTime.now();
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

    public void markAsWaitingPickup() {
        if (status != ShipmentStatus.IN_TRANSIT) {
            throw new ShipmentValidationException(
                    "El envio no se ha enviado a destino, no puede ser entregado al cliente");
        }
        status = ShipmentStatus.WAITING_PICKUP;
    }

    public void markRegistered() {
        status = ShipmentStatus.REGISTERED;
    }

    public void markInTransit() {
        status = ShipmentStatus.IN_TRANSIT;
    }

    public void assignToVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new ShipmentValidationException("El vehículo no puede ser nulo");
        }
        // Ejemplo de regla: solo asignar si el envío está registrado
        if (this.status != ShipmentStatus.REGISTERED) {
            throw new ShipmentValidationException("Solo se pueden asignar envíos en estado REGISTERED");
        }
        this.vehicle = vehicle;
    }

    public void removeFromVehicle() {
        this.vehicle = null;
    }

}
