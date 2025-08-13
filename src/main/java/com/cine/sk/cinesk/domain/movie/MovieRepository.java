package com.cine.sk.cinesk.domain.movie;


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

    List<MovieEntity> findByFeaturedTrue();

    Optional<MovieEntity> findBySlug(String slug);

    @Query("UPDATE MovieEntity SET deletedAt = :deleteAt WHERE uuid = :uuid")
    void deleteById(LocalDateTime deleteAt, UUID uuid);

    @Query("SELECT m FROM MovieEntity m WHERE m.deletedAt IS NULL AND (LOWER(m.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(m.director) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<MovieEntity> searchByTitleOrDirector(@org.springframework.data.repository.query.Param("q") String q, Pageable pageable);

    @Query("SELECT m FROM MovieEntity m WHERE m.deletedAt IS NULL AND (:genre IS NULL OR EXISTS (SELECT g FROM m.genres g WHERE g.name = :genre)) AND (:category IS NULL OR m.category.name = :category) AND (:yearMin IS NULL OR m.releaseYear >= :yearMin) AND (:yearMax IS NULL OR m.releaseYear <= :yearMax)")
    List<MovieEntity> filterFilms(@org.springframework.data.repository.query.Param("genre") String genre,
                                  @org.springframework.data.repository.query.Param("yearMin") Integer yearMin,
                                  @org.springframework.data.repository.query.Param("yearMax") Integer yearMax,
                                  @org.springframework.data.repository.query.Param("category") String category);


}
