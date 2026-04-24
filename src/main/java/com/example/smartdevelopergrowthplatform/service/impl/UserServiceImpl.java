package com.example.smartdevelopergrowthplatform.service.impl;

import com.example.smartdevelopergrowthplatform.dto.UserRequestDTO;
import com.example.smartdevelopergrowthplatform.dto.UserResponseDTO;
import com.example.smartdevelopergrowthplatform.entity.User;
import com.example.smartdevelopergrowthplatform.exception.BadRequestException;
import com.example.smartdevelopergrowthplatform.exception.ResourceNotFoundException;
import com.example.smartdevelopergrowthplatform.repository.UserRepository;
import com.example.smartdevelopergrowthplatform.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new BadRequestException("Email already registered: " + userRequestDTO.getEmail());
        }

        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setGoal(userRequestDTO.getGoal());
        user.setLevel(userRequestDTO.getLevel());

        User savedUser = userRepository.save(user);
        return mapToUserResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return mapToUserResponseDTO(user);
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getGoal(),
                user.getLevel()
        );
    }
}
