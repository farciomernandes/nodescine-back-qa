package com.cine.sk.cinesk.domain.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, UUID> {

    @Query("SELECT g FROM GenreEntity g WHERE g.deletedAt IS NULL")
    List<GenreEntity> findAllActive();

    @Query("SELECT g FROM GenreEntity g WHERE g.uuid = :uuid AND g.deletedAt IS NULL")
    Optional<GenreEntity> findActiveById(UUID uuid);

    Optional<GenreEntity> findByName(String name);

    @Query("UPDATE GenreEntity SET deletedAt = :deleteAt WHERE uuid = :uuid")
    void deleteById(LocalDateTime deleteAt, UUID uuid);
}