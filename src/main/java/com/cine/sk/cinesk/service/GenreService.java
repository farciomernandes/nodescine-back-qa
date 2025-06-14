package com.cine.sk.cinesk.service;

import com.cine.sk.cinesk.dto.GenreDTO;
import com.cine.sk.cinesk.entity.GenreEntity;
import com.cine.sk.cinesk.repository.GenreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
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
                GenreEntity genre = objectMapper.convertValue(dto, GenreEntity.class);
                GenreEntity saved = genreRepository.save(genre);
                return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.convertValue(saved, GenreDTO.class));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public void deleteById(UUID uuid) {
        genreRepository.deleteById(LocalDateTime.now(), uuid);
    }

    private boolean validateGenreExistsByName(String genreName) {
        Optional<GenreEntity> genre = genreRepository.findByName(genreName);
        return genre.isPresent();
    }

    public void validateGenreExists(UUID genreUuid) {
        genreRepository.findById(genreUuid)
                .orElseThrow(() -> new NoSuchElementException("Genre not found"));
    }

    public ResponseEntity<List<GenreDTO>> getAll() {
        List<GenreDTO> genres = genreRepository.findAllActive().stream()
                .map(genre -> objectMapper.convertValue(genre, GenreDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(genres);
    }
}