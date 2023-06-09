package com.movies.movieslist.movie;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @Column(unique = true)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Float duration;
    @NotNull
    private String year;
}
