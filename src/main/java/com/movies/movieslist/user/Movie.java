package com.movies.movieslist.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Float duration;
    @NotNull
    private String year;
}
