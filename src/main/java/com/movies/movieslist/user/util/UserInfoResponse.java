package com.movies.movieslist.user.util;

import com.movies.movieslist.movie.Movie;
import com.movies.movieslist.user.User;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserInfoResponse {

    private String username;

    @Nullable
    private String email;

    private String country;
    private List<Movie> movies;

    private float hoursViewed;

    @Nullable
    private String imageFilename;


    @Nullable
    private List<User> followers;

    @Nullable
    public List<User> following;

}
