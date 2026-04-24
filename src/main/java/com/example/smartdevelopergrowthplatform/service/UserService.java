package com.example.smartdevelopergrowthplatform.service;

import com.example.smartdevelopergrowthplatform.dto.UserRequestDTO;
import com.example.smartdevelopergrowthplatform.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(Long userId);
}
