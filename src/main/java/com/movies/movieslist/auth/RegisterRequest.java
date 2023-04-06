package com.movies.movieslist.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = "The username must be not null")
    private String username;
    //private String firstname;
    //private String lastname;
    @NotNull(message = "The email must be not null")
    private String email;

    @NotNull(message = "The country must be not null")
    private String country;

    @NotNull(message = "The password must be not null")
    private String password;
    @NotNull(message = "You must to confirm the password")

    private String passwordConfirm;
}
