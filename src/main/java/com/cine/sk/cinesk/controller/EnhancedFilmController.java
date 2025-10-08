package com.cine.sk.cinesk.controller;

import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enhanced-films")
@Tag(name = "Films", description = "Endpoints para gerenciamento de filmes")
public class EnhancedFilmController {

    private final EnhancedFilmService enhancedFilmService;

    @GetMapping("/{id}")
    public ResponseEntity<EnhancedFilmDTO> findById(
            @Parameter(description = "Movie identifier", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(enhancedFilmService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os filmes com filtros opcionais",
            description = "Retorna uma lista de filmes. Pode ser filtrado por título e/ou diretor.")
    public ResponseEntity<List<EnhancedFilmDTO>> getAllFilms(

            @Parameter(description = "Filtrar filmes pelo título. Não diferencia maiúsculas/minúsculas.",
                    example = "Inception")
            @RequestParam(required = false) String title,

            @Parameter(description = "Filtrar filmes pelo nome do diretor. Não diferencia maiúsculas/minúsculas.",
                    example = "Christopher Nolan")
            @RequestParam(required = false) String director,

            // --- MUDANÇA PRINCIPAL AQUI ---
            @Parameter(description = "Filtrar por um ou mais gêneros. Repita o parâmetro para múltiplos valores (ex: ?genres=Action&genres=Sci-Fi).",
                    example = "Action")
            @RequestParam(required = false) List<String> genres,

            // --- MUDANÇA PRINCIPAL AQUI ---
            @Parameter(description = "Filtrar por uma category.",
                    example = "Lançamento")
            @RequestParam(required = false) String category, // Renomeado para 'categories' (plural) para clareza

            @Parameter(description = "Filtrar filmes por um ator ou atriz do elenco. Não diferencia maiúsculas/minúsculas.",
                    example = "Vin Diesel")
            @RequestParam(required = false) String cast,
            @Parameter(description = "Campo de busca geral (search) que procura o termo em vários campos do seu filme ao mesmo tempo.. Não diferencia maiúsculas/minúsculas.",
                    example = "Alan Turning")
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(enhancedFilmService.findAll(search, title, director, genres, category, cast));
    }

    @Transactional
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EnhancedFilmDTO> create(
            @Valid @RequestPart("dto") EnhancedFilmDTO dto,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        var created = enhancedFilmService.create(dto);
        if (file != null && !file.isEmpty()) {
            var movieWithPoster = enhancedFilmService.insertPoster(created.getId(), file);
            return ResponseEntity.ok(movieWithPoster);
        }

        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnhancedFilmDTO> update(@PathVariable Long id, @Valid @RequestBody EnhancedFilmDTO dto) {
        return ResponseEntity.ok(enhancedFilmService.update(id, dto));
    }

    @PostMapping(value = "/poster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertPoster(
            @RequestParam("id") Long id,
            @RequestParam("file") MultipartFile file) {
       enhancedFilmService.insertPoster(id, file);
       return ResponseEntity.ok("Poster inserted successfully");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enhancedFilmService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
