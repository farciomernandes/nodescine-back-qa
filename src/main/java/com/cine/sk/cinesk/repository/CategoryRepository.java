package com.cine.sk.cinesk.repository;

import com.cine.sk.cinesk.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    @Query("SELECT c FROM CategoryEntity c WHERE c.deletedAt IS NULL")
    List<CategoryEntity> findAllActive();

    @Query("SELECT c FROM CategoryEntity c WHERE c.uuid = :uuid AND c.deletedAt IS NULL")
    Optional<CategoryEntity> findActiveById(UUID uuid);

    Optional<CategoryEntity> findBySlug(String slug);
}
