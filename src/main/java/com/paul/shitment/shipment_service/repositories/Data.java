package com.paul.shitment.shipment_service.repositories;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Office;
import com.paul.shitment.shipment_service.models.entities.Person;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class Data {

    private final PersonRepository personRepository;
    private final OfficeRepository officeRepository;
    private final UserRepository userRepository;

    public void createData() {
        Person personSender = new Person("Juana Lopez Sanches", "7834091", "12345678");
        Person personRecipient = new Person("Paul Rodrigo Guasace Yovio", "8072061", "63414902");

        Office origginOffice = new Office("Satelite Cooperativa de transporte 15 de abril",
                "B/Santos Dumont, C/Velasco, #123", "00000000");

        Office destinationOffice = new Office("Montero Cooperativa de transporte 15 de abril",
                "B/San simon, C/Los Candas, #34", "11111111");

        Person personUser = new Person("Pablo Escobar", "5678905", "78023456");
        AppUser user1 = new AppUser("pablito", "root_1234", "pablito@gmail.com", personUser);

        personRepository.save(personSender);
        personRepository.save(personRecipient);

        officeRepository.save(origginOffice);
        officeRepository.save(destinationOffice);

        personRepository.save(personUser);
        userRepository.save(user1);

    }
}
