package com.cine.sk.cinesk.domain.movie.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private UUID uuid;
    private String name;
    private String slug;
    private String imageUrl;
}
