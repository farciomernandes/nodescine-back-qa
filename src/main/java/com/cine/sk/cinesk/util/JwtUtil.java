package com.cine.sk.cinesk.util;


import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.auth.UserTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private Key key;

    private final UserTokenService userTokenService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    @PostConstruct
    public void init() {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
            this.key = Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JWT key", e);
        }
    }

    public String generateToken(User user) {
        logger.info("Generating token for user: {}", user.getEmail());
        try {
            var issuedAt = new Date();
            var expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("roles", user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                    .setIssuedAt(issuedAt)
                    .setExpiration(expiresAt)
                    .signWith(key)
                    .compact();

            userTokenService.saveToken(
                    user.getEmail(),
                    token,
                    convertToLocalDateTime(issuedAt),
                    convertToLocalDateTime(expiresAt)
            );

            return token;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token for user: " + user.getEmail(), e);
        }
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        var claims = getClaimsFromToken(token);
        return claims.get("roles", ArrayList.class);
    }

    public boolean isTokenValid(String token) {
        try {
            logger.info("Validating token");
            getClaimsFromToken(token);
            boolean isActive = userTokenService.isTokenActive(token);

            if (isActive) {
                String email = getEmailFromToken(token);
                logger.info("Token is valid for user: {}", email);
            } else {
                logger.info("Token is inactive in database");
            }

            return isActive;
        } catch (Exception e) {
            logger.info("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public void invalidateToken(String token) {
        try {
            String email = getEmailFromToken(token);
            logger.info("Invalidating token for user: {}", email);
            userTokenService.invalidateToken(token);
            logger.info("Token invalidated successfully for user: {}", email);
        } catch (Exception e) {
            logger.warn("Failed to invalidate token: {}", e.getMessage());
            userTokenService.invalidateToken(token);
        }
    }
}
