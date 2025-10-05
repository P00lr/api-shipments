package com.paul.shitment.shipment_service.mappers;

import java.util.ArrayList;
import java.util.List;

import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.models.entities.AppUser;
import com.paul.shitment.shipment_service.models.entities.Person;

public class UserMapper {

    public static UserResponseDto entityToDto(AppUser user) {
        return new UserResponseDto(
            user.getId(), 
            user.getUsername(), 
            user.getEmail(), 
            user.isActive());
    }

    public static List<UserResponseDto> entitiesToDto(List<AppUser> users) {
        List<UserResponseDto> listUsers = new ArrayList<UserResponseDto>();

        for (int i = 0; i < users.size(); i++) {
            AppUser user = users.get(i);
            UserResponseDto userDto = entityToDto(user);
            listUsers.add(userDto);
        }

        return listUsers;

    }

    public static AppUser dtoToEntity(UserRequestDto userDto, Person person) {
        return new AppUser(
            userDto.username(),
            userDto.password(),
            userDto.email(),
            person
        );
    }
}
