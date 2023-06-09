package com.movies.movieslist.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.movies.movieslist.movie.Movie;
import com.movies.movieslist.user.util.UserInfoResponse;
import com.movies.movieslist.user.util.UserUpdateInfoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Return the info of the user.",tags = {"User"})
    @GetMapping("/info")
    @ResponseBody
    public ResponseEntity<UserInfoResponse> getUserInfo(){
        UserInfoResponse userInfo=userService.getUserInfo();

        return  new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

    @Operation(summary = "Return the info of the user searched.",tags = {"User"})
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<UserInfoResponse> searchUser(@RequestParam("username") String username){
        UserInfoResponse userInfo=userService.searchUser(username);

        return  new ResponseEntity<>(userInfo,HttpStatus.OK);
    }


    @Operation(summary = "Return the list of movies of the user.",tags = {"User"},security = {@SecurityRequirement(name="BearerJWT")})
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<Movie>> getUserList(){
        List<Movie> userMovies=userService.getUserMovies();

        return  new ResponseEntity<>(userMovies,HttpStatus.OK);
    }

    @Operation(summary = "Post a movie on the list of the user.",tags = {"User"},security = {@SecurityRequirement(name="BearerJWT")})
    @PostMapping("/movie")
    @ResponseBody
    public ResponseEntity<Movie> addMovieToUser(@RequestBody Movie movie){
        Movie movieToAdd=null;
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            movieToAdd=userService.addMovieToUser(movie);
       }

        return new ResponseEntity<>(movieToAdd,HttpStatus.OK);
    }

    @Operation(summary = "Follow a user.",tags = {"User"},security = {@SecurityRequirement(name="BearerJWT")})
    @PostMapping("/follow")
    @ResponseBody
    public ResponseEntity<HttpStatus> followUser(@RequestBody ObjectNode username){
        User userFollow=userService.followUser(username.get("username").asText());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Delete a movie on the list of the user.",tags = {"User"},security = {@SecurityRequirement(name="BearerJWT")})
    @DeleteMapping("/movie")
    @ResponseBody
    public ResponseEntity<Movie> removeUserMovie(@RequestParam("id") Long movieID){

        return new ResponseEntity<>(userService.deleteUserMovie(movieID),HttpStatus.OK);
    }

    @Operation(summary = "Update the info of the user.",tags = {"User"},security = {@SecurityRequirement(name="BearerJWT")})
    @PutMapping
    @ResponseBody
    public ResponseEntity<UserInfoResponse> putUser(@RequestBody UserUpdateInfoRequest userUpdateInfoRequest){
        userService.putUserInfo(userUpdateInfoRequest);
        UserInfoResponse userInfo=userService.getUserInfo();

        return new ResponseEntity<>(userInfo,HttpStatus.OK);
    }

}