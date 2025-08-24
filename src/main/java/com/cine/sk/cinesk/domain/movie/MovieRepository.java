package com.cine.sk.cinesk.domain.movie;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Page<MovieEntity> findAllActive(Pageable pageable);

    @Query("SELECT DISTINCT m FROM MovieEntity m LEFT JOIN m.genres g " +
           "WHERE m.deletedAt IS NULL " +
           "AND (:search IS NULL OR :search = '' OR " +
           "     LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "     LOWER(m.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:isPremium IS NULL OR m.premium = :isPremium) " +
           "AND (:genres IS NULL OR :genres = '' OR g.name IN :genreList)")
    Page<MovieEntity> findAllActiveWithFilters(
        @Param("search") String search,
        @Param("isPremium") Boolean isPremium,
        @Param("genres") String genres,
        @Param("genreList") List<String> genreList,
        Pageable pageable);

    @Query("SELECT m FROM MovieEntity m WHERE m.uuid = :uuid AND m.deletedAt IS NULL")
    Optional<MovieEntity> findActiveById(UUID uuid);

    List<MovieEntity> findByFeaturedTrue();

    Optional<MovieEntity> findBySlug(String slug);

    @Modifying
    @Query("UPDATE MovieEntity SET deletedAt = :deleteAt WHERE uuid = :uuid")
    void deleteById(LocalDateTime deleteAt, UUID uuid);

    @Query("SELECT m FROM MovieEntity m WHERE m.deletedAt IS NULL AND (LOWER(m.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(m.director) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<MovieEntity> searchByTitleOrDirector(@org.springframework.data.repository.query.Param("q") String q, Pageable pageable);

    @Query("SELECT DISTINCT m FROM MovieEntity m LEFT JOIN m.genres g " +
           "WHERE m.deletedAt IS NULL " +
           "AND (:genre IS NULL OR g.name = :genre) " +
           "AND (:category IS NULL OR m.category.name = :category) " +
           "AND (:yearMin IS NULL OR m.releaseYear >= :yearMin) " +
           "AND (:yearMax IS NULL OR m.releaseYear <= :yearMax)")
    List<MovieEntity> filter(@Param("genre") String genre,
                             @Param("yearMin") Integer yearMin,
                             @Param("yearMax") Integer yearMax,
                             @Param("category") String category);

    @Query("SELECT m FROM MovieEntity m WHERE m.deletedAt IS NULL ORDER BY m.createdAt DESC")
    List<MovieEntity> findNewReleases();

    @Query("SELECT m FROM MovieEntity m WHERE m.deletedAt IS NULL ORDER BY m.viewCount DESC, m.rating DESC")
    List<MovieEntity> findPopular(Pageable pageable);


}
