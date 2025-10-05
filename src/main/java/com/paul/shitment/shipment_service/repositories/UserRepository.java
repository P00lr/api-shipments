package com.paul.shitment.shipment_service.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paul.shitment.shipment_service.models.entities.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID>{
    boolean existsByUsername(String name);
    boolean existsByPassword(String password);
    boolean existsByEmail(String email);
}
