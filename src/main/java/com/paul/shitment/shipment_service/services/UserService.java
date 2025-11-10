package com.paul.shitment.shipment_service.services;

import java.util.List;
import java.util.UUID;

import com.paul.shitment.shipment_service.dto.PageResponse;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;
import com.paul.shitment.shipment_service.dto.user.UserUpdateRequestDto;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    PageResponse<UserResponseDto> getAllUsersPaged(
            int page,
            int size,
            String sortBy);

    UserResponseDto getUserByid(UUID id);

    UserResponseDto createUser(UserRequestDto userDto);

    UserResponseDto updateUser(UUID id, UserUpdateRequestDto userDto);

    UserResponseDto dactivateUser(UUID id);

}
