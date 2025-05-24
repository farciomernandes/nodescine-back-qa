package com.cine.sk.cinesk.repository;

import com.cine.sk.cinesk.entity.MovieEntity;
import com.cine.sk.cinesk.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT m FROM UserEntity m WHERE m.deletedAt IS NULL")
    Page<List<UserEntity>> findAllActive(Pageable pageable);
}
