package com.cine.sk.cinesk.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    
    Optional<UserToken> findByToken(String token);

    @Modifying
    @Query("UPDATE UserToken t SET t.active = false WHERE t.userEmail = :userEmail AND t.active = true")
    void deactivateAllUserTokens(String userEmail);
    
    @Modifying
    @Query("UPDATE UserToken t SET t.active = false WHERE t.token = :token")
    void deactivateToken(String token);
}