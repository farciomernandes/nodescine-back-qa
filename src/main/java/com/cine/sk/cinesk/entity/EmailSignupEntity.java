package com.cine.sk.cinesk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "email_signup_tbl")
@Getter
@Setter
public class EmailSignupEntity extends AbstractEntity {

    @Column(unique = true)
    private String email;
}