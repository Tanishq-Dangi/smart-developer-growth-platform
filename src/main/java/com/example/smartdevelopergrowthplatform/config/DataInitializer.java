package com.example.smartdevelopergrowthplatform.config;

import com.example.smartdevelopergrowthplatform.entity.User;
import com.example.smartdevelopergrowthplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User defaultUser = new User();
            defaultUser.setName("Tarun");
            defaultUser.setEmail("tarun@test.com");
            defaultUser.setPassword("Temp@12345");
            defaultUser.setGoal("Backend");
            defaultUser.setLevel("Beginner");

            userRepository.save(defaultUser);
            System.out.println("Default user created");
        }
    }
}
