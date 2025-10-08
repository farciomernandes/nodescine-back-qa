package com.cine.sk.cinesk.domain.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT DISTINCT m FROM Movie m " +
            "LEFT JOIN m.genres g " +
            "LEFT JOIN m.category c " +
            "LEFT JOIN m.cast ac " +
            "WHERE (:search IS NULL OR :search = '' OR (" +
            "   lower(m.title) LIKE lower(concat('%', :search, '%')) OR " +
            "   lower(m.description) LIKE lower(concat('%', :search, '%')) OR " +
            "   lower(m.director) LIKE lower(concat('%', :search, '%')) OR " +
            "   lower(g.name) LIKE lower(concat('%', :search, '%')) OR " +
            "   lower(c.name) LIKE lower(concat('%', :search, '%')) OR " +
            "   lower(ac) LIKE lower(concat('%', :search, '%'))" +
            ")) " +
            "AND (:title IS NULL OR :title = '' OR lower(m.title) LIKE lower(concat('%', :title, '%'))) " +
            "AND (:director IS NULL OR :director = '' OR lower(m.director) LIKE lower(concat('%', :director, '%'))) " +
            "AND (:genres IS NULL OR :genres = '' OR g.name IN :genres) " +
            "AND (:category IS NULL OR :category = '' OR lower(c.name) = lower(:category)) " +
            "AND (:cast IS NULL OR :cast = '' OR lower(ac) LIKE lower(concat('%', :cast, '%')))")
    List<Movie> findAllByFilters(
            @Param("search") String search,
            @Param("title") String title,
            @Param("director") String director,
            @Param("genres") List<String> genres,
            @Param("category") String category,
            @Param("cast") String cast
    );
}