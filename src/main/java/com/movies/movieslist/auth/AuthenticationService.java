package com.movies.movieslist.auth;

import com.movies.movieslist.config.JwtService;
import com.movies.movieslist.token.Token;
import com.movies.movieslist.token.TokenRepository;
import com.movies.movieslist.user.RoleUser;
import com.movies.movieslist.user.User;
import com.movies.movieslist.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor

public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        var user= User.builder().firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleUser.USER)
                .enabled(false)
                .build();

        var savedUser=repository.save(user);
        var jwtToken=jwtService.generateToken(user);
        saveUserToken(savedUser,jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken=jwtService.generateToken(user);
        saveUserToken(user,jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }



    private void saveUserToken(User user, String jwtToken){
        var token= Token.builder()
                .user(user)
                .token(jwtToken)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }
}
