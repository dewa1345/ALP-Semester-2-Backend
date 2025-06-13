package com.EcoV.ALP.service;

import com.EcoV.ALP.dto.AuthResponse;
import com.EcoV.ALP.dto.LoginRequest;
import com.EcoV.ALP.dto.RegisterRequest;
import com.EcoV.ALP.entity.User;
import com.EcoV.ALP.repository.UserRepository;
import com.EcoV.ALP.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse registerUser(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Email already registered.")
                    .build();
        }

        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setVerified(false); // or true if you don't implement verification

            User savedUser = userRepository.save(user);

            String token = jwtUtil.generateToken(savedUser);

            return AuthResponse.builder()
                    .success(true)
                    .message("Registration successful.")
                    .id_user(savedUser.getId_user())
                    .name(savedUser.getName())
                    .email(savedUser.getEmail())
                    .token(token)
                    .build();

        } catch (Exception e) {
            e.printStackTrace(); // Log full error to console
            return AuthResponse.builder()
                    .success(false)
                    .message("Registration failed: " + e.getMessage())
                    .build();
        }
    }

    public AuthResponse loginUser(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid email or password.")
                    .build();
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return AuthResponse.builder()
                    .success(false)
                    .message("Invalid email or password.")
                    .build();
        }

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .success(true)
                .message("Login successful.")
                .id_user(user.getId_user())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }
}
