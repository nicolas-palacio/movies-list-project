package com.movies.movieslist.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5500/")
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getUsers(){

        return ResponseEntity.ok(userService.getUsers());
    }
}
