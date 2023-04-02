package com.movies.movieslist.auth;


import com.movies.movieslist.email.EmailService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Confirm the registration of the user.",tags = {"Authentication"})
    @GetMapping("/register/confirm")
    public String confirm(@RequestParam("token") String token){
        return authenticationService.confirmToken(token);
    }


    @Operation(summary = "Login of the user.",tags = {"Authentication"})
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
