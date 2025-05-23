package com.cine.sk.cinesk.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MovieCategoriesDTO {
    UUID movieUuid;
    UUID categoryUuid;
}
