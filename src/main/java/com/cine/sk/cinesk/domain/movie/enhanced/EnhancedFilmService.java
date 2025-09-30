package com.cine.sk.cinesk.domain.movie.enhanced;

import com.cine.sk.cinesk.domain.file.aws.AwsService;
import com.cine.sk.cinesk.domain.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnhancedFilmService {

    private final MovieService movieService;
    private final AwsService awsService;

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
        return movieService.update(id, dto);
    }

    public void insertPoster(Long id, MultipartFile file) {
        var uploaded = awsService.upload(file, "poster", id.toString(), file.getName());
        movieService.insertPoster(uploaded, id);
    }

    public void delete(Long id) {
        movieService.delete(id);
    }

    private void validateCreateEnhancedFilmDTO(EnhancedFilmDTO dto) {
        if (dto.getGenres() == null || dto.getGenres().isEmpty()) {
            throw new IllegalArgumentException("Precisa ter pelo menos um genero");
        }
    }
}
