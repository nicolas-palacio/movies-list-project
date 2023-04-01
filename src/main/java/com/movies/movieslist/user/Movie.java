package com.movies.movieslist.user;

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
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Float duration;
    @NotNull
    private String year;
}
