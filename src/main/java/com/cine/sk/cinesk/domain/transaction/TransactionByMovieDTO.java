package com.cine.sk.cinesk.domain.transaction;


import com.cine.sk.cinesk.domain.movie.Movie;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionByMovieDTO {

    private BigDecimal totalAmount;
    Movie movie;

}
