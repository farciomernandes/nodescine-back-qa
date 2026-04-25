package com.cine.sk.cinesk.domain.movie.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {
    private String reporterEmail;
    private String reason;
}

