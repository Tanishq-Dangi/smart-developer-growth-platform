package com.example.smartdevelopergrowthplatform.config;

import com.example.smartdevelopergrowthplatform.entity.User;
import com.example.smartdevelopergrowthplatform.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User defaultUser = new User();
            defaultUser.setName("Tarun");
            defaultUser.setEmail("tarun@test.com");
            defaultUser.setPassword(passwordEncoder.encode("Temp@12345"));
            defaultUser.setGoal("Backend");
            defaultUser.setLevel("Beginner");

            userRepository.save(defaultUser);
            System.out.println("Default user created");
        }
    }
}
