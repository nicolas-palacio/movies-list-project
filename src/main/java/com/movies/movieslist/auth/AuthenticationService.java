package com.movies.movieslist.auth;

import com.movies.movieslist.auth.util.EmailValidator;
import com.movies.movieslist.security.JwtService;
import com.movies.movieslist.security.exceptions.BadRequestException;
import com.movies.movieslist.security.exceptions.ForbiddenException;
import com.movies.movieslist.email.EmailService;
import com.movies.movieslist.email.confirm_token.ConfirmationToken;
import com.movies.movieslist.email.confirm_token.ConfirmationTokenRepository;
import com.movies.movieslist.email.confirm_token.ConfirmationTokenService;
import com.movies.movieslist.token.Token;
import com.movies.movieslist.token.TokenRepository;
import com.movies.movieslist.user.RoleUser;
import com.movies.movieslist.user.User;
import com.movies.movieslist.user.UserRepository;
import com.movies.movieslist.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    private final UserRepository userRepository;

    private final ConfirmationTokenService confirmationTokenService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;
    private final EmailValidator emailValidator;

    public AuthenticationResponse register(RegisterRequest request) {
        if(requestIsEmpty(request)){
            throw new BadRequestException("All inputs are empty");
        }

        validateUsername(request);

        if(!emailValidator.test(request.getEmail())){
            throw new BadRequestException("Invalid email or already taken");
        }

        if(request.getCountry().isEmpty()){
            throw new BadRequestException("You must provide your country");
        }

        if(request.getPassword().length()<8){
            throw new BadRequestException("The password length must have at least 8 characters");
        }

        if(!request.getPassword().equals(request.getPasswordConfirm())){
            throw new BadRequestException("The passwords are different");
        }

        var user= User.builder().username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleUser.USER)
                .enabled(false)
                .build();

        var savedUser= userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);

        saveUserTokenConfirmation(savedUser,jwtToken);
        emailService.send(request.getEmail(),jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private boolean requestIsEmpty(RegisterRequest request) {
        return request.getUsername().isEmpty() && request.getPassword().isEmpty()  && request.getCountry().isEmpty() &&
                request.getEmail().isEmpty()  && request.getPasswordConfirm().isEmpty() ;
    }

    private void validateUsername(RegisterRequest request) {
        Optional<User> user=userRepository.findByUsername(request.getUsername());

        if(!user.isEmpty()){
            throw new BadRequestException("Username already taken");
        }

        if(validateUsernameLenght(request.getUsername())){
            throw new BadRequestException("Username is too long (maximum is 15 characters)");
        }

        if(!validateUsernameChars(request.getUsername())){
            throw new BadRequestException("Username may only contain alphanumeric characters");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> appUser=userRepository.findByEmail(request.getEmail());
        if(appUser.isEmpty()){
            throw new BadRequestException("User not found");
        }

        if(!appUser.get().getEnabled()){
            throw new ForbiddenException("Email not confirmed");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

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

    private void saveUserTokenConfirmation(User user,String jwtToken){
        var token=new ConfirmationToken(jwtToken,LocalDateTime.now(),LocalDateTime.now().plusMinutes(15),user);

        confirmationTokenRepository.save(token);

    }

    //@Transactional
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

    public String refreshToken(){
        if(!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            throw new ForbiddenException("User not authenticated");
        }

        String email= SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository.findByEmail(email).orElseThrow();
        var jwtToken=jwtService.generateToken(user);
        saveUserToken(user,jwtToken);

        return jwtToken;
    }

    private boolean validateUsernameLenght(String username){
        return username.length()>15;
    }

    private boolean validateUsernameChars(String username){
        return StringUtils.isAlphanumeric(username);
    }
}
