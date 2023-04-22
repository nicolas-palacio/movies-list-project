package com.movies.movieslist.user.util;

import com.movies.movieslist.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserInfoResponse {

    private String username;

    private String email;

    private String country;
    private List<Movie> movies;

    private float hoursViewed;

    private String imageFilename;

}
