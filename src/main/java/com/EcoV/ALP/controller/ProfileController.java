package com.EcoV.ALP.controller;

import com.EcoV.ALP.dto.ProfileDTO;
import com.EcoV.ALP.entity.User;
import com.EcoV.ALP.repository.UserRepository;
import com.EcoV.ALP.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile") // you can change this prefix if needed
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ProfileDTO> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // Strip "Bearer " prefix
            String token = authHeader.replace("Bearer ", "");

            // Get user ID from token
            Long userId = jwtUtil.extractUserId(token); // Make sure this method exists

            // Find the user in DB
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            // Map to DTO
            ProfileDTO dto = new ProfileDTO();
            dto.setName(user.getName());
            dto.setPfp(user.getFotoProfil());
            dto.setStat(user.isVerified());

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.status(401).build(); // Invalid token, unauthorized
        }
    }
}
