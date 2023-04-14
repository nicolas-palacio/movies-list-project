package com.movies.movieslist.user.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserUpdateInfoRequest {
    private String username;

    private String email;

    private String country;

    private String currentPassword;
    private String password;

    private String passwordConfirm;

}
