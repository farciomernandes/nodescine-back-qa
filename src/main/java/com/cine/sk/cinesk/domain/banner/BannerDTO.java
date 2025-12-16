package com.cine.sk.cinesk.domain.banner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BannerDTO {
    private String label;
    private BannerType type;
    private String url;
}
