package com.cine.sk.cinesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "watch_access")
@Getter
@Setter
public class WatchAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "uuid")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "movieId", referencedColumnName = "uuid")
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(name = "paymentReference", referencedColumnName = "uuid")
    private Payment payment;

    @Column
    private LocalDateTime accessExpiresAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}