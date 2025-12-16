package com.cine.sk.cinesk.domain.banner;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
public class Banner extends AbstractEntity {

    @Column(nullable = false)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BannerType type;

    @Column(nullable = false)
    private String url;

    @JsonCreator
    public Banner(@JsonProperty("label") String label, 
                  @JsonProperty("type") BannerType type, 
                  @JsonProperty("url") String url) {
        this.label = label;
        this.type = type;
        this.url = url;
    }
}
