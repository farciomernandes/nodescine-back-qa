package com.cine.sk.cinesk.domain.movie.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    private UUID uuid;

    private String name;
}