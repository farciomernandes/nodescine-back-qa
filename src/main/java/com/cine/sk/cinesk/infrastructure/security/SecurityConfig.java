package com.cine.sk.cinesk.infrastructure.security;

import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.user.service.CustomUserDetailsService;
import com.cine.sk.cinesk.infrastructure.cors.CorsProperties;
import com.cine.sk.cinesk.infrastructure.jwt.JwtAuthenticationFilter;
import com.cine.sk.cinesk.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationEntryPoint customAuthEntryPoint;

    @Bean
    public CorsFilter corsFilter(CorsProperties corsProperties) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(corsProperties.getAllowedOrigins()));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register/customer",
                                "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/test/**", "/asaas/webhook/**").permitAll()
                        .requestMatchers("/auth/register/moderator").hasAnyAuthority(Role.MOVIE_DIRECTOR.name(), Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.POST, "/enhanced-films", "/enhanced-films/**").hasAnyAuthority(Role.MOVIE_DIRECTOR.name(), Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.GET, "/transactions/director/", "/transactions/director/**").hasAnyAuthority(Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.PUT, "/enhanced-films", "/enhanced-films/**").hasAnyAuthority(Role.MOVIE_DIRECTOR.name(), Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.GET, "/enhanced-films", "/enhanced-films/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/all").hasAnyAuthority(Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/genres", "/genres/**").hasAnyAuthority(Role.MOVIE_DIRECTOR.name(), Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.GET, "/genres", "/genres/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint)  // Para 401
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}