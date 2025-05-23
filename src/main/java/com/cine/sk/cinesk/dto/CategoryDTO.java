package com.cine.sk.cinesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {
    private UUID uuid;
    private String name;
    private String slug;
}
