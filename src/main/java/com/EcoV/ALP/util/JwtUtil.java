package com.EcoV.ALP.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;
import com.EcoV.ALP.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your-very-secure-secret-key-which-should-be-long-enough";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId_user())
                .claim("name", user.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            return false;
        }
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Long extractUserId(String token) {
    try {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object rawId = claims.get("id");

        System.out.println("✅ Extracted raw ID: " + rawId + " (" + rawId.getClass().getName() + ")");

        if (rawId instanceof Integer) {
            return ((Integer) rawId).longValue();
        } else if (rawId instanceof Long) {
            return (Long) rawId;
        } else {
            System.out.println("⚠️ Unexpected ID type");
            return null;
        }

    } catch (Exception e) {
        System.out.println("❌ Error decoding JWT:");
        e.printStackTrace(); // 🔥 this is the most important log now
        return null;
    }
}


}
