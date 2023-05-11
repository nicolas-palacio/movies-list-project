package com.movies.movieslist.auth;


import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register the user.",tags = {"Authentication"})
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){


        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(summary = "Confirm the registration of the user.",tags = {"Authentication"},security = {@SecurityRequirement(name="BearerJWT")})
    @GetMapping("/register/confirm")
    public String confirm(@RequestParam("token") String token){
        return authenticationService.confirmToken(token);
    }

    @Operation(summary = "Send the email to confirm user account.",tags = {"Authentication"})
    @PostMapping("/email/confirm")
    public ResponseEntity<Object>sendConfirmationEmail(@RequestBody ObjectNode email){
        String userEmail=email.get("email").asText();

        authenticationService.resendConfirmationEmail(userEmail);

        return null;
    }


    @Operation(summary = "Login of the user.",tags = {"Authentication"})
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @Operation(summary = "Refresh the token of an user already authenticated.",tags = {"Authentication"},security = {@SecurityRequirement(name="BearerJWT")})
    @GetMapping("/token/refresh")
    public ResponseEntity<String> refreshToken(){

        return ResponseEntity.ok(authenticationService.refreshToken());
    }

}
