package com.paul.shitment.shipment_service.models.entities;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;
    
    @Column(unique = true)
    private String email;
    
    private boolean active = true;

    @OneToOne
    @JoinColumn(name = "person_id", unique = true)
    private Person person;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"),
        uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private Set<Role> roles = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = false )
    private Office office;

    @JsonManagedReference
    @OneToMany(mappedBy = "createdBy")
    private List<Shipment> shipmentsCreated = new ArrayList<>();

    public AppUser(String username, String password, String email, Person person) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.person = person;
        active = true;
    }

    public void updateFromRequestDto(UserUpdateRequestDto userDto) {
        if (!userDto.name().equals(this.getPerson().getName()))
            this.getPerson().setName(userDto.name().trim());

        if (!userDto.phone().equals(this.getPerson().getPhone()))
            this.getPerson().setPhone(userDto.phone().trim());

        if (!userDto.ci().equals(this.getPerson().getCi()))
            this.getPerson().setCi(userDto.ci().trim());

        if (!userDto.username().equals(this.getUsername()))
            this.setUsername(userDto.username().trim());

        if (!userDto.email().equals(this.getEmail()))
            this.setEmail(userDto.email().trim());
    }

}
