package com.paul.shitment.shipment_service.models.entities;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.role.RoleRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String name;
    private boolean enabled = true;

    @ManyToMany
    @JoinTable(
        name = "roles_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id"),
        uniqueConstraints = {@UniqueConstraint(columnNames = {"role_id", "permission_id"})}
    )
    private Set<Permission> permissions = new LinkedHashSet<>();

    public void updateFromRoleRequestDto(RoleRequestDto roleDto) {
        if(!name.equals("ROLE_" + roleDto.name().trim().toUpperCase()))
            name = "ROLE_" + roleDto.name().trim().toUpperCase();
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
