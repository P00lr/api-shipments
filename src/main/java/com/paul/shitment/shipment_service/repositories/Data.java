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
        Person personSender = new Person();

        personSender.setName("Juana Lopez Sanches");
        personSender.setCi("7834091");
        personSender.setPhone("12345678");

        Person personRecipient = new Person();
        personRecipient.setName("Paul Rodrigo Guasace Yovio");
        personRecipient.setCi("8072061");
        personRecipient.setPhone("63414902");

        Office origginOffice = new Office();

        origginOffice.setName("Satelite Cooperativa de transporte 15 de abril");
        origginOffice.setAddress("B/Santos Dumont, C/Velasco, #123");
        origginOffice.setPhone("00000000");

        Office destinationOffice = new Office();

        destinationOffice.setName("Montero Cooperativa de transporte 15 de abril");
        destinationOffice.setAddress("B/San simon, C/Los Candas, #34");
        destinationOffice.setPhone("11111111");


        Person personUser = new Person();
        personUser.setName("Pablo Escobar");
        personUser.setCi("5678905");
        personUser.setPhone("78023456");

        AppUser user1 = new AppUser("pablito", "root_1234", "pablito@gmail.com", personUser);

        personRepository.save(personSender);
        personRepository.save(personRecipient);

        officeRepository.save(origginOffice);
        officeRepository.save(destinationOffice);

        personRepository.save(personUser);
        userRepository.save(user1);

    }
}
