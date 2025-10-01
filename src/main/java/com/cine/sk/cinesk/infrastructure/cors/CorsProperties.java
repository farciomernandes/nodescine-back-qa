package com.cine.sk.cinesk.infrastructure.cors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CorsProperties {

    @Value("${CORS_ORIGINS:http://localhost:3000}")
    private String allowedOrigins;

    @Value("${CORS_HEADERS}")
    private String allowedHeaders;

    @Value("${CORS_METHODS}")
    private String allowedMethods;

}
