package com.cine.sk.cinesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payment_user_movie")
@Getter
@Setter
public class PaymentUserMovie extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "uuid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "uuid")
    private MovieEntity movie;

    @Column
    private String gatewayId;

    @Column
    private String paymentProvider;

    @Column
    private String accountInfo;
}
