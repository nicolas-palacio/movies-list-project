package com.movies.movieslist.auth;

import com.movies.movieslist.config.JwtService;
import com.movies.movieslist.email.confirm_token.ConfirmationToken;
import com.movies.movieslist.email.confirm_token.ConfirmationTokenService;
import com.movies.movieslist.token.Token;
import com.movies.movieslist.token.TokenRepository;
import com.movies.movieslist.user.RoleUser;
import com.movies.movieslist.user.User;
import com.movies.movieslist.user.UserRepository;
import com.movies.movieslist.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor

public class AuthenticationService {

    private final UserService userService;

    private final UserRepository repository;

    private final ConfirmationTokenService confirmationTokenService;
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

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }
}
