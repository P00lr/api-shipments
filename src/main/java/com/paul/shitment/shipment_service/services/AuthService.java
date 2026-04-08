package com.paul.shitment.shipment_service.services;

import com.paul.shitment.shipment_service.dto.auth.LoginRequestDto;
import com.paul.shitment.shipment_service.dto.auth.LoginResponseDto;
import com.paul.shitment.shipment_service.dto.user.UserRequestDto;
import com.paul.shitment.shipment_service.dto.user.UserResponseDto;


public interface AuthService {

    UserResponseDto registerUser(UserRequestDto userDto);

    LoginResponseDto login(LoginRequestDto request);
    
    void logout(String username);
}
