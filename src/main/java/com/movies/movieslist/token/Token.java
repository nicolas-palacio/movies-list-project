package com.movies.movieslist.token;

import com.movies.movieslist.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue
    public Long id;

    @Column(unique=true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    public boolean revoked;
    public boolean expired;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    public User user;


}
