package com.cine.sk.cinesk.infrastructure.repositorys;

import com.cine.sk.cinesk.domain.banner.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
}
