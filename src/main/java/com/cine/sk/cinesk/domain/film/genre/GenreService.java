package com.cine.sk.cinesk.domain.film.genre;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final ObjectMapper objectMapper;

    public ResponseEntity<GenreDTO> create(GenreDTO dto) {
        try {
            boolean result = validateGenreExistsByName(dto.getName());
            if (!result) {
                Genre genre = objectMapper.convertValue(dto, Genre.class);
                Genre saved = genreRepository.save(genre);
                return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.convertValue(saved, GenreDTO.class));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public void deleteById(Long id) {
        genreRepository.deleteById(LocalDateTime.now(), id);
    }

    private boolean validateGenreExistsByName(String genreName) {
        Optional<Genre> genre = genreRepository.findByName(genreName);
        return genre.isPresent();
    }

    public void validateGenreExists(Long id) {
        genreRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Genre not found"));
    }

    public ResponseEntity<List<GenreDTO>> getAll() {
        List<GenreDTO> genres = genreRepository.findAllActive().stream()
                .map(genre -> objectMapper.convertValue(genre, GenreDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(genres);
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    public Genre save(GenreDTO genreDTO) {
        return genreRepository.save(objectMapper.convertValue(genreDTO, Genre.class));
    }

    public Optional<Genre> findByName(String name) {
        return genreRepository.findByName(name);
    }
}