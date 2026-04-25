package com.cine.sk.cinesk.domain.movie;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT DISTINCT m FROM Movie m " +
            "LEFT JOIN m.genres g " +
            "LEFT JOIN m.category c " +
            "LEFT JOIN m.cast ac " +
            "WHERE m.active = true AND (:search IS NULL OR :search = '' OR " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(m.director) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(g.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(ac) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Movie> findAllByFilters(@Param("search") String search, Pageable pageable);

    @Query("SELECT DISTINCT m FROM Movie m " +
            "LEFT JOIN m.genres g " +
            "LEFT JOIN m.category c " +
            "LEFT JOIN m.cast ac " +
            "WHERE m.active = true " +
            "AND (:title IS NULL OR :title = '' OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:description IS NULL OR :description = '' OR LOWER(m.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:director IS NULL OR :director = '' OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%'))) " +
            "AND (:genre IS NULL OR :genre = '' OR LOWER(g.name) LIKE LOWER(CONCAT('%', :genre, '%'))) " +
            "AND (:category IS NULL OR :category = '' OR LOWER(c.name) = LOWER(:category)) " +
            "AND (:cast IS NULL OR :cast = '' OR LOWER(ac) LIKE LOWER(CONCAT('%', :cast, '%')))")
    Page<Movie> findAllByFilters(
            @Param("title") String title,
            @Param("description") String description,
            @Param("director") String director,
            @Param("genre") String genre,
            @Param("category") String category,
            @Param("cast") String cast,
            Pageable pageable
    );

    Page<Movie> findAllByActiveTrue(Pageable pageable);

    List<Movie> findByCreatedBy(String createdBy);

    Optional<Movie> findBySlug(String slug);

    boolean existsBySlug(String slug);

}
