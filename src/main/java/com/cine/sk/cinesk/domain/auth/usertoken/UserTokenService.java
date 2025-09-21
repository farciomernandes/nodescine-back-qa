package com.cine.sk.cinesk.domain.auth.usertoken;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private static final Logger logger = LoggerFactory.getLogger(UserTokenService.class);

    private final UserTokenRepository userTokenRepository;

    @Transactional
    public void saveToken(String userEmail, String token, LocalDateTime issuedAt, LocalDateTime expiresAt) {
        try {
            userTokenRepository.deactivateAllUserTokens(userEmail);

            var userToken = UserToken.builder()
                    .userEmail(userEmail)
                    .token(token)
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .active(true)
                    .build();

            userTokenRepository.save(userToken);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save token for user: " + userEmail, e);
        }
    }

    @Transactional
    public void invalidateToken(String token) {
        try {
            userTokenRepository.deactivateToken(token);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invalidate token", e);
        }
    }

    public boolean isTokenActive(String token) {
        try {
            boolean isActive = userTokenRepository.findByToken(token)
                    .map(UserToken::isActive)
                    .orElse(false);

            if (isActive) {
                logger.info("Token is active");
            } else {
                logger.info("Token is not active or not found");
            }

            return isActive;
        } catch (Exception e) {
            logger.error("Error checking token status", e);
            return false;
        }
    }
}
