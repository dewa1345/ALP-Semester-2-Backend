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
            return new AuthResponse(false, "Email already registered.");
        }

        User user = new User();
        user.setName(request.getName()); // assuming `setName` exists in the entity
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setVerified(false);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser);

        return AuthResponse.builder()
                .message("Registration successful.")
                .success(true)
                .id_user(savedUser.getId_user()) // ensure your User entity uses `getId()`
                .name(savedUser.getName()) // and `getName()` instead of `getNama()`
                .email(savedUser.getEmail())
                .token(token)
                .build();
    }

    public AuthResponse loginUser(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            return new AuthResponse(false, "Invalid email or password.");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(false, "Invalid email or password.");
        }

        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .message("Login successful.")
                .success(true)
                .id_user(user.getId_user())
                .name(user.getName())
                .email(user.getEmail())
                .token(token)
                .build();
    }
}
