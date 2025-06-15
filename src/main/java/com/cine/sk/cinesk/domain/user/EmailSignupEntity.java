package com.cine.sk.cinesk.domain.user;

import com.cine.sk.cinesk.domain.AbstractEntity;
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