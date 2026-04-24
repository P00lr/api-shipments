package com.paul.shitment.shipment_service.models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;

import com.paul.shitment.shipment_service.dto.office.OfficeRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "office")
public class Office {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    private String address;

    @Column(unique = true)
    private String phone;

    private boolean active = true;
    
    @OneToMany(mappedBy = "originOffice")
    private List<Shipment> originShipments = new ArrayList<>();

    @OneToMany(mappedBy = "destinationOffice")
    private List<Shipment> destinationShipments = new ArrayList<>();

    public void updateFromRequestDto(OfficeRequestDto officeDto) {
        this.setName(officeDto.name());
        this.setAddress(officeDto.address());
        this.setPhone(officeDto.phone());
    }
}
