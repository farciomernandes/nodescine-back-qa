package com.cine.sk.cinesk.domain.movie.enhanced;

import com.cine.sk.cinesk.domain.file.aws.AwsService;
import com.cine.sk.cinesk.domain.movie.MovieService;
import com.cine.sk.cinesk.domain.user.User;
import com.cine.sk.cinesk.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnhancedFilmService {

    private final MovieService movieService;
    private final AwsService awsService;
    private final UserService userService;

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado ou não encontrado");
        }
        String email = auth.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autorizado ou não encontrado"));
    }

    public Page<EnhancedFilmDTO> findAll(String searchTerm, Pageable pageable) {
        return movieService.findAll(searchTerm, pageable);
    }

    public EnhancedFilmDTO findById(Long id) {
        return movieService.findById(id);
    }

    public EnhancedFilmDTO findBySlug(String slug) {
        return movieService.findBySlug(slug);
    }

    public List<EnhancedFilmDTO> findMyMovies() {
        User user = currentUser();
        return movieService.findByUserEmail(user.getEmail());
    }

    public EnhancedFilmDTO create(EnhancedFilmDTO dto) {
        validateCreateEnhancedFilmDTO(dto);
        return movieService.create(dto);
    }

    public EnhancedFilmDTO update(Long id, EnhancedFilmDTO dto) {
        return movieService.update(id, dto);
    }

    public EnhancedFilmDTO insertPoster(Long id, MultipartFile file) {
        var uploaded = awsService.upload(file, "poster", id.toString(), file.getName());
        return movieService.insertPoster(uploaded, id);
    }

    public void delete(Long id) {
        movieService.delete(id);
    }

    private void validateCreateEnhancedFilmDTO(EnhancedFilmDTO dto) {
        if (dto.getGenres() == null || dto.getGenres().isEmpty()) {
            throw new IllegalArgumentException("Precisa ter pelo menos um genero");
        }
    }

    public Page<EnhancedFilmDTO> findAll(String title, String description, String director, String genre, String category, String cast, Pageable pageable) {
        return movieService.findAll(title, description, director, genre, category, cast, pageable);
    }
}
