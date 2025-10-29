package com.paul.shitment.shipment_service.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paul.shitment.shipment_service.models.entities.Person;


@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    boolean existsByCi(String ci);
    boolean existsByPhone(String phone);
    Optional<Person> findByCi(String ci);
    Optional<Person> findByPhone(String phone);
}
