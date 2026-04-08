package com.paul.shitment.shipment_service.models.entities;

import java.util.UUID;

import com.paul.shitment.shipment_service.dto.permission.PermissionRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String name;
    private boolean enabled = true;

    public void updateFromPermissionRequestDto(PermissionRequestDto permissionDto) {
        if(!name.equals(permissionDto.name()))
            name = permissionDto.name();
    }

    public void deactivate(){
        if(enabled == false)
            enabled = true;
    }

    public void activate() {
        if(enabled == true)
            enabled = false;
    }
}
