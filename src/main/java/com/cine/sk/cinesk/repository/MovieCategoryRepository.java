package com.cine.sk.cinesk.repository;

import com.cine.sk.cinesk.entity.MovieCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieCategoryRepository extends JpaRepository<MovieCategoryEntity, UUID> {

    @Query("SELECT mc FROM MovieCategoryEntity mc WHERE mc.deletedAt IS NULL")
    List<MovieCategoryEntity> findAllActive();

    @Query("SELECT mc FROM MovieCategoryEntity mc WHERE mc.movie.uuid = :uuid AND mc.deletedAt IS NULL")
    List<MovieCategoryEntity> findByMovieId(UUID uuid);
}
