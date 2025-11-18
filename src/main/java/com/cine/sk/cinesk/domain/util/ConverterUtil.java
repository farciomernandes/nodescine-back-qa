package com.cine.sk.cinesk.domain.util;

import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.genre.GenreDTO;

import java.util.stream.Collectors;

public class ConverterUtil {
    public static EnhancedFilmDTO movieToEnhancedFilmDTO(Movie entity) {
        EnhancedFilmDTO dto = new EnhancedFilmDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDirector(entity.getDirector());
        dto.setYear(entity.getYear());
        dto.setCategory(entity.getCategory() != null ? entity.getCategory().getName() : null);
        dto.setGenres(entity.getGenres().stream()
            .map(genre -> new GenreDTO(genre.getId(), genre.getName()))
            .collect(Collectors.toList()));
        dto.setDuration(minutesToDuration(entity.getDurationInMinutes()));
        dto.setMovieUrl(entity.getMovieUrl());
        dto.setTrailerUrl(entity.getTrailer());
        dto.setPrice(entity.getPrice());
        dto.setSynopsis(entity.getDescription());
        dto.setPoster(entity.getPoster());
        dto.setCast(entity.getCast());
        dto.setSlug(entity.getSlug());
        dto.setIsAdultConfirmed(entity.getIsAdultConfirmed());
        dto.setProducerDeadline(entity.getProducerDeadline());
        dto.setMovieType(entity.getMovieType());
        return dto;
    }

    public static String minutesToDuration(Integer minutes) {
        if (minutes == null) return null;
        long hours = minutes / 60;
        long mins = minutes % 60;
        return String.format("%dh %dm", hours, mins);
    }
}
