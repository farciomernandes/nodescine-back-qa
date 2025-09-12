package com.cine.sk.cinesk.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.cors")
@Getter
@Setter
public class CorsProperties {

    @Value("${security.cors.origins}")
    private String allowedOrigins;

    @Value("${security.cors.headers}")
    private String allowedHeaders;

    @Value("${security.cors.methods}")
    private String allowedMethods;

}
