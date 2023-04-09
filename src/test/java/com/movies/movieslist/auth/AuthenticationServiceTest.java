package com.movies.movieslist.auth;


import com.movies.movieslist.auth.util.EmailValidator;
import com.movies.movieslist.user.User;
import com.movies.movieslist.user.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailValidator emailValidator;

    @InjectMocks
    private AuthenticationService authenticationService;
    private User user;


    void setup(){
        this.user=User.builder()
                .username("nicolaspalacio")
                .email("nicolas@gmail.com")
                .password("secure126")
                .enabled(false)
                .build();
    }

    @DisplayName("Test to save an user")
    @Test
    void registerUserTest(){
        //given
       RegisterRequest registerRequest=new RegisterRequest("nicolas2023","nico.p22013@gmail.com","Argentina","secure123","secure123");

        //when
        authenticationService.register(registerRequest);
        User savedUser=userRepository.findByEmail(registerRequest.getEmail()).get();

        //then
        assertThat(savedUser).isNotNull();




    }
}
