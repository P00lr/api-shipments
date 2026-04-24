package com.paul.shitment.shipment_service.models.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.transportCooperative.TransportCooperativeRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "transport_cooperatives")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransportCooperative {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @Column(name = "enabled")
    private boolean enabled = true;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "transport_cooperative_id")
    private Set<Office> offices = new HashSet<>();

    public void updateFromShipmentUpdateRequestDto(TransportCooperativeRequest request) {
        this.name = request.name();
        this.enabled = request.enabled();
    }

    public void deactivate() {
        enabled = false;
    }

}
