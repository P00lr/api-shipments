package com.paul.shitment.shipment_service.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.paul.shitment.shipment_service.dto.person.PersonRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Person;

@Component
public class UserMapper {

    public UserResponseDto entityToDto(AppUser user) {
        return new UserResponseDto(
            user.getId(), 
            user.getPerson().getName(),
            user.getPerson().getPhone(),
            user.getPerson().getCi(),
            user.getUsername(), 
            user.getEmail(), 
            user.isActive());
    }

    public List<UserResponseDto> entitiesToDto(List<AppUser> users) {
        List<UserResponseDto> listUsers = new ArrayList<UserResponseDto>();

        for (int i = 0; i < users.size(); i++) {
            AppUser user = users.get(i);
            UserResponseDto userDto = entityToDto(user);
            listUsers.add(userDto);
        }

        return listUsers;
    }

    public AppUser dtoToEntity(UserRequestDto userDto, Person person) {
        return new AppUser(
            userDto.username(),
            userDto.password(),
            userDto.email(),
            person
        );
    }

    public PersonRequestDto userRequestToPersonRequest(UserRequestDto userDto) {
        return new PersonRequestDto(
            userDto.name(),
            userDto.ci(), 
            userDto.phone());
    }
}
