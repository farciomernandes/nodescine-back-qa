package com.cine.sk.cinesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "uuid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "uuid")
    private MovieEntity movie;

    @Column
    private float amountPaid;

    @Column
    private float systemFee;

    @Column
    private float producerAmount;

    @Enumerated(EnumType.STRING)
    @Column
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @Column
    private LocalDateTime paymentDate;

    public enum PaymentMethod {
        CREDIT, PIX
    }

    public enum Status {
        PENDING, PAID, FAILED, REFUND
    }
}