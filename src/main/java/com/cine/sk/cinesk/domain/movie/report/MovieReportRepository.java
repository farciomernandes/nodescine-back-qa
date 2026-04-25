package com.cine.sk.cinesk.domain.movie.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieReportRepository extends JpaRepository<MovieReport, Long> {
}

