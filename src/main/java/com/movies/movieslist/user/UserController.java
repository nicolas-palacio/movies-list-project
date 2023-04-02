package com.movies.movieslist.user;

import com.movies.movieslist.user.util.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<Object> getUserInfo(){
        UserInfoResponse userInfo=userService.getUserInfo();

        return  new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Movie>> getUserList(){
        List<Movie> userMovies=userService.getUserMovies();

        return  new ResponseEntity<>(userMovies,HttpStatus.OK);
    }

    @PostMapping("/addMovie")
    public ResponseEntity<Object> addMovieToUser(@RequestBody Movie movie){
        Movie movieToAdd=null;
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            movieToAdd=userService.addMovieToUser(movie);
       }

        return new ResponseEntity<Object>(movieToAdd,HttpStatus.OK);
    }

}