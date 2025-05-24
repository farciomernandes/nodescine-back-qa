package com.cine.sk.cinesk.repository;


import com.cine.sk.cinesk.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, UUID> {

    @Query("SELECT m FROM MovieEntity m WHERE m.deletedAt IS NULL")
    Page<List<MovieEntity>> findAllActive(Pageable pageable);

    @Query("SELECT m FROM MovieEntity m WHERE m.uuid = :uuid AND m.deletedAt IS NULL")
    Optional<MovieEntity> findActiveById(UUID uuid);

    Optional<MovieEntity> findBySlug(String slug);

    @Query("UPDATE MovieEntity SET deletedAt = :deleteAt WHERE uuid = :uuid")
    void deleteById(LocalDateTime deleteAt, UUID uuid);


}
