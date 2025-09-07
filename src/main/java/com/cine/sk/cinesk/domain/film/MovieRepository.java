package com.cine.sk.cinesk.domain.film;


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
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.deletedAt IS NULL")
    Page<Movie> findAllActive(Pageable pageable);

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN m.genres g " +
           "WHERE m.deletedAt IS NULL " +
           "AND (:search IS NULL OR :search = '' OR " +
           "     LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "     LOWER(m.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:isPremium IS NULL OR m.premium = :isPremium) " +
           "AND (:genres IS NULL OR :genres = '' OR g.name IN :genreList)")
    Page<Movie> findAllActiveWithFilters(
        @Param("search") String search,
        @Param("isPremium") Boolean isPremium,
        @Param("genres") String genres,
        @Param("genreList") List<String> genreList,
        Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.id = :id AND m.deletedAt IS NULL")
    Optional<Movie> findActiveById(Long id);

    List<Movie> findByFeaturedTrue();

    Optional<Movie> findBySlug(String slug);

    @Modifying
    @Query("UPDATE Movie SET deletedAt = :deleteAt WHERE id = :id")
    void deleteById(LocalDateTime deleteAt, Long id);

    @Query("SELECT m FROM Movie m WHERE m.deletedAt IS NULL AND (LOWER(m.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(m.director) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Movie> searchByTitleOrDirector(@org.springframework.data.repository.query.Param("q") String q, Pageable pageable);

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN m.genres g " +
           "WHERE m.deletedAt IS NULL " +
           "AND (:genre IS NULL OR g.name = :genre) " +
           "AND (:category IS NULL OR m.category.name = :category) " +
           "AND (:yearMin IS NULL OR m.releaseYear >= :yearMin) " +
           "AND (:yearMax IS NULL OR m.releaseYear <= :yearMax)")
    List<Movie> filter(@Param("genre") String genre,
                             @Param("yearMin") Integer yearMin,
                             @Param("yearMax") Integer yearMax,
                             @Param("category") String category);

    @Query("SELECT m FROM Movie m WHERE m.deletedAt IS NULL ORDER BY m.createdAt DESC")
    List<Movie> findNewReleases();

    @Query("SELECT m FROM Movie m WHERE m.deletedAt IS NULL ORDER BY m.viewCount DESC, m.rating DESC")
    List<Movie> findPopular(Pageable pageable);


}
