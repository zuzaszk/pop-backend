package com.pop.backend.auth;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pop.backend.entity.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(Users user, Integer role) {
        
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", role)
                .claim("id", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))  // 1 day
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateResetToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // 1 hour
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateInvitationToken(String email, String roleName, Integer projectId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role_name", roleName)
                .claim("project_id", projectId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1209600000))  // 14 days
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTemporaryToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))  // 1 hour
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public Integer getRoleFromToken(String token) {
        return (Integer) validateToken(token).get("role", Integer.class);
    }

    public Integer getIdFromToken(String token) {
        return (Integer) validateToken(token).get("id", Integer.class);
    }
}

