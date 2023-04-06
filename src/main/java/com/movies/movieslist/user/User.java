package com.movies.movieslist.user;

import com.movies.movieslist.movie.Movie;
import com.movies.movieslist.token.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
@Getter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
   // private String firstname;
    //private String lastname;
    private String email;
    private String password;

    private String country;

    @Enumerated(EnumType.STRING)
    private RoleUser role;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_movies", joinColumns = @JoinColumn(name="user_email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "movie_id",referencedColumnName = "id"))
    private List<Movie> movies= new ArrayList<>();
    private float hoursViewed;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    private Boolean enabled=false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
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
        return true;
    }
}
