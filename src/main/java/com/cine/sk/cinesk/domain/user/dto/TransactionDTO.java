package com.cine.sk.cinesk.domain.user.dto;

import com.cine.sk.cinesk.domain.movie.Movie;
import lombok.*;

@Getter
@Setter
@Builder
public class TransactionDTO {

    private Long transactionId;

    private Movie movie;
}
