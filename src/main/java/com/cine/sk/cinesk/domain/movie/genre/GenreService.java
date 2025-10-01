package com.cine.sk.cinesk.domain.movie.genre;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final ObjectMapper objectMapper;

    public void deleteById(Long id) {
        genreRepository.deleteById(LocalDateTime.now(), id);
    }

    public ResponseEntity<List<GenreDTO>> getAll() {
        List<GenreDTO> genres = genreRepository.findAllActive().stream()
                .map(genre -> objectMapper.convertValue(genre, GenreDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(genres);
    }

    public Genre save(GenreDTO genreDTO) {
        return genreRepository.save(objectMapper.convertValue(genreDTO, Genre.class));
    }

    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }
}