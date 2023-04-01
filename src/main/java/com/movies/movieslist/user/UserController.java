package com.movies.movieslist.user;


import com.movies.movieslist.user.util.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(){
        UserInfoResponse userInfo=null;
        

        return new ResponseEntity<>(userInfo,HttpStatus.ACCEPTED);
        //userService.getUsers()
    }

    @PostMapping("/addMovie")//@RequestHeader("Authorization") String token
    public ResponseEntity<Object> addMovieToUser(@RequestBody Movie movie){
        Movie movieToAdd=null;
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            movieToAdd=userService.addMovieToUser(movie);
       }

        return new ResponseEntity<Object>(movieToAdd,HttpStatus.OK);
    }

}
