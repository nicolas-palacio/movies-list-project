package com.movies.movieslist.user;

import com.movies.movieslist.config.JwtService;
import com.movies.movieslist.config.exceptions.NotFoundException;
import com.movies.movieslist.user.util.UserInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    private final JwtService jwtService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> appUser = userRepository.findByEmail(email);
        if(appUser==null){
            throw new NotFoundException("User not founded");
        }

        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(appUser.get().getRole().toString()));

        return new org.springframework.security.core.userdetails.User(appUser.get().getUsername(),appUser.get().getPassword(),authorities);
    }

    public UserInfoResponse getUserInfo(){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByEmail(email);
        UserInfoResponse userInfo=new UserInfoResponse(user.get().getFirstname(),user.get().getLastname(),user.get().getEmail(),user.get().getCountry(),user.get().getMovies(),user.get().getHoursViewed());

        return userInfo;
    }

    public List<Movie> getUserMovies(){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByEmail(email);

        List<Movie> movies=user.get().getMovies();

        return movies;

    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public int enableUser(String email){
        return userRepository.enableAppUser(email);
    }

    public Movie addMovieToUser(Movie movie){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByEmail(email);
        Optional<Movie> savedMovie=movieRepository.findById(movie.getId());

        if(savedMovie.isEmpty()){
            movieRepository.save(movie);
            user.get().getMovies().add(movie);
            userRepository.save(user.get());
        }

        return movie;
    }


}
