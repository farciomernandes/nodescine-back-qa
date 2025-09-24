package com.cine.sk.cinesk.domain.user;

import com.cine.sk.cinesk.domain.AbstractEntity;
import com.cine.sk.cinesk.domain.auth.enums.Role;
import com.cine.sk.cinesk.domain.movie.Movie;
import com.cine.sk.cinesk.domain.transaction.Transaction;
import com.cine.sk.cinesk.domain.transaction.TransactionStatus;
import com.cine.sk.cinesk.domain.user.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER ,cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    // Filmes adquiridos pelo usuário (derivado das transações ativas)
    @Transient
    public List<Movie> getAcquiredMovies() {
        return transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.ACTIVE && t.getMovie() != null)
                .map(Transaction::getMovie)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE;
    }
}