package com.cine.sk.cinesk.domain.transaction;


import com.cine.sk.cinesk.domain.movie.enhanced.EnhancedFilmDTO;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByMovieDTO {

    private BigDecimal totalAmount;
    EnhancedFilmDTO movie;

}
