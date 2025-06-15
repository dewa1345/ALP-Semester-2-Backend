package com.EcoV.ALP.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.EcoV.ALP.dto.AuthRequest;
import com.EcoV.ALP.dto.AuthResponse;
import com.EcoV.ALP.dto.RegisterRequest;
import com.EcoV.ALP.entity.User;
import com.EcoV.ALP.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Register a new user
     */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "Email already in use");
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setVerified(false); // Default to false

        userRepository.save(newUser);

        return new AuthResponse(true, "User registered successfully");
    }

    /**
     * Authenticate a user
     */
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Invalid email or password");
        }

        return new AuthResponse(true, "Login successful");
    }
    public UserRepository getUserRepository() {return this.userRepository;}
}
