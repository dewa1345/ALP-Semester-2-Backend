package com.EcoV.ALP.controller;

import com.EcoV.ALP.dto.ProfileDTO;
import com.EcoV.ALP.entity.User;
import com.EcoV.ALP.repository.UserRepository;
import com.EcoV.ALP.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*") // ✅ Allow frontend to send Authorization header
public class ProfileController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<ProfileDTO> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            System.out.println("🔐 AUTH HEADER: " + authHeader);

            // Remove "Bearer " prefix and trim
            String token = authHeader.replace("Bearer", "").trim();
            System.out.println("🔐 TOKEN: " + token);

            // Extract user ID from JWT token
            Long userId = jwtUtil.extractUserId(token);
            System.out.println("👤 Extracted user ID: " + userId);

            if (userId == null) {
                System.out.println("❌ Invalid or missing user ID in token");
                return ResponseEntity.status(401).build();
            }

            // Find the user in the database
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found in DB"));

            // Build and return the profile DTO
            ProfileDTO dto = new ProfileDTO();
            dto.setName(user.getName());
            dto.setPfp(user.getFotoProfil());
            dto.setStat(user.isVerified());
            dto.setId_user(user.getId_user()); // ✅ Include user ID

            System.out.println("✅ Sending Profile DTO: " + dto.getName() + ", ID: " + dto.getId_user());

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            System.out.println("❌ Exception in getProfile:");
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }
}
