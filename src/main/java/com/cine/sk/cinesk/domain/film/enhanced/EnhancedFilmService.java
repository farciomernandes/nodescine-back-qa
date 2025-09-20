package com.cine.sk.cinesk.domain.film.enhanced;

import com.cine.sk.cinesk.domain.film.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        // Validar presença de pelo menos um gênero na criação
        if (dto.getGenres() == null || dto.getGenres().isEmpty()) {
            throw new IllegalArgumentException("Precisa ter pelo menos um genero");
        }
        // Validar formato de rentalPrice (ex.: "4.99")
        if (dto.getRentalPrice() != null && !Pattern.matches("\\d+\\.\\d{2}", dto.getRentalPrice())) {
            throw new IllegalArgumentException("Invalid rentalPrice format: " + dto.getRentalPrice() + ". Expected format: '4.99'");
        }
    }

    private void validateUpdateEnhancedFilmDTO(EnhancedFilmDTO dto) {
        // Para update, apenas validar campos fornecidos; campos ausentes permanecem inalterados
        if (dto.getRentalPrice() != null && !Pattern.matches("\\d+\\.\\d{2}", dto.getRentalPrice())) {
            throw new IllegalArgumentException("Invalid rentalPrice format: " + dto.getRentalPrice() + ". Expected format: '4.99'");
        }
    }
}
