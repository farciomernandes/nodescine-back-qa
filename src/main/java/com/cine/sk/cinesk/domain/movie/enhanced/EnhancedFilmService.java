package com.cine.sk.cinesk.domain.movie.enhanced;

import com.cine.sk.cinesk.domain.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EnhancedFilmService {

    private final MovieService movieService;

    public List<EnhancedFilmDTO> findAll() {
        return movieService.findAll();
    }

    public EnhancedFilmDTO findById(Long id) {
        return movieService.findById(id);
    }

    public EnhancedFilmDTO create(EnhancedFilmDTO dto) {
        validateCreateEnhancedFilmDTO(dto);
        return movieService.create(dto);
    }

    public EnhancedFilmDTO update(Long id, EnhancedFilmDTO dto) {
        validateUpdateEnhancedFilmDTO(dto);
        return movieService.update(id, dto);
    }

    public void delete(Long id) {
        movieService.delete(id);
    }

    private void validateCreateEnhancedFilmDTO(EnhancedFilmDTO dto) {
        if (dto.getGenres() == null || dto.getGenres().isEmpty()) {
            throw new IllegalArgumentException("Precisa ter pelo menos um genero");
        }
        if (dto.getRentalPrice() != null && !Pattern.matches("\\d+\\.\\d{2}", dto.getRentalPrice())) {
            throw new IllegalArgumentException("Invalid rentalPrice format: " + dto.getRentalPrice() + ". Expected format: '4.99'");
        }
    }

    private void validateUpdateEnhancedFilmDTO(EnhancedFilmDTO dto) {
        if (dto.getRentalPrice() != null && !Pattern.matches("\\d+\\.\\d{2}", dto.getRentalPrice())) {
            throw new IllegalArgumentException("Invalid rentalPrice format: " + dto.getRentalPrice() + ". Expected format: '4.99'");
        }
    }
}
