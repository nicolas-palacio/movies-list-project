package com.movies.movieslist.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<HttpStatus> getUsers(){

        return ResponseEntity.ok(HttpStatus.OK);
        //userService.getUsers()
    }

    @PostMapping("/addMovie")
    public ResponseEntity<Movie> addMovieToUser(@RequestHeader("Authorization") String authHeader,
                                                @RequestBody Movie movieToAdd  ){
        

    }

}
