package com.movies.movieslist.user;

import com.movies.movieslist.auth.RegisterRequest;
import com.movies.movieslist.auth.util.EmailValidator;
import com.movies.movieslist.movie.Movie;
import com.movies.movieslist.movie.MovieRepository;
import com.movies.movieslist.security.JwtService;
import com.movies.movieslist.security.exceptions.BadRequestException;
import com.movies.movieslist.security.exceptions.NotFoundException;
import com.movies.movieslist.user.util.UserInfoResponse;
import com.movies.movieslist.user.util.UserUpdateInfoRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final EmailValidator emailValidator;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> appUser = userRepository.findByEmail(email);
        if(appUser==null){
            throw new NotFoundException("User not founded.");
        }

        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(appUser.get().getRole().toString()));

        return new org.springframework.security.core.userdetails.User(appUser.get().getUsername(),appUser.get().getPassword(),authorities);
    }

    public UserInfoResponse getUserInfo(){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByEmail(email);
        UserInfoResponse userInfo=null;

        if(user.get().getImage()==null){
            userInfo=new UserInfoResponse(user.get().getUserAuthName(),user.get().getEmail(),user.get().getCountry(),
                    user.get().getMovies(),user.get().getHoursViewed(),null,user.get().getFollowers(),user.get().getFollowings());
        }else{
            userInfo=new UserInfoResponse(user.get().getUserAuthName(),user.get().getEmail(),user.get().getCountry(),
                    user.get().getMovies(),user.get().getHoursViewed(),user.get().getImage().getFilename(),user.get().getFollowers(),user.get().getFollowings());
        }

        return userInfo;
    }

    public UserInfoResponse searchUser(String username){
        Optional<User> user=userRepository.findByUsername(username);

        if(user.isEmpty()){
            throw new NotFoundException("User not found.");
        }

        return null;

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

    public User followUser(String userToFollow){
        Optional<User> user=userRepository.findByUsername(userToFollow);
        if(user.isEmpty()){
            throw new NotFoundException("User not found.");
        }

        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userLogged=userRepository.findByEmail(email);

        if(userLogged.get().getUsername().equals(userToFollow)){
            throw new BadRequestException("User can not follow itself.");
        }

        user.get().getFollowers().add(userLogged.get());
        userLogged.get().getFollowings().add(user.get());

        userRepository.save(user.get());
        userRepository.save(userLogged.get());

        return user.get();

    }
    public Movie addMovieToUser(Movie movie){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByEmail(email);
        Optional<Movie> savedMovie=movieRepository.findById(movie.getId());

        if(savedMovie.isEmpty()){
            movieRepository.save(movie);
        }

        user.get().getMovies().add(movie);
        user.get().setHoursViewed(user.get().getHoursViewed()+movie.getDuration());
        userRepository.save(user.get());

        return movie;
    }

    public Movie deleteUserMovie(Long movieID){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByEmail(email);
        Optional<Movie> savedMovie=movieRepository.findById(movieID);

        user.get().getMovies().remove(savedMovie.get());
        user.get().setHoursViewed(user.get().getHoursViewed()-savedMovie.get().getDuration());
        userRepository.save(user.get());

        return  savedMovie.get();

    }

    public User putUserInfo(UserUpdateInfoRequest updateInfo){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> updateUser=userRepository.findByEmail(email);

        if(updateUser.isEmpty()){
            throw new NotFoundException("User not founded");
        }

        if(updateInfo.getEmail()!=null){
            /*if(emailValidator.test(updateInfo.getEmail())){
                //TO DO send confirm email
                email=updateInfo.getEmail();
            }*/
        }

        if(updateInfo.getUsername()!=null){
            if(validateUsername(updateInfo.getUsername())){
                updateUser.get().setUsername(updateInfo.getUsername());
            }
        }

        if(updateInfo.getCountry()!=null){
            updateUser.get().setCountry(updateInfo.getCountry());
        }


        if(updateInfo.getCurrentPassword()!=null){
            if(!passwordEncoder.matches(updateInfo.getCurrentPassword(),updateUser.get().getPassword())){
                throw new BadRequestException("The current password is invalid.");
            }
        }

        if(updateInfo.getCurrentPassword()!=null && (updateInfo.getPassword().length()<8 || updateInfo.getPasswordConfirm().length()<8)){
            throw new BadRequestException("Minimum length is 8 characters.");
        }


        if(updateInfo.getPassword()!=null && updateInfo.getPasswordConfirm()!=null){
            if(updateInfo.getPassword().equals(updateInfo.getPasswordConfirm())){
                updateUser.get().setPassword(passwordEncoder.encode(updateInfo.getPassword()));
            }else{
                throw new BadRequestException("The passwords are different.");
            }
        }

        userRepository.save(updateUser.get());

        return userRepository.findByEmail(email).get();

    }

    private boolean validateUsername(String username) {
        Optional<User> user=userRepository.findByUsername(username);

        if(!user.isEmpty()){
            throw new BadRequestException("Username already taken.");
        }

        if(validateUsernameLenght(username)){
            throw new BadRequestException("Username is too long (maximum is 15 characters).");
        }

        if(!validateUsernameChars(username)){
            throw new BadRequestException("Username may only contain alphanumeric characters.");
        }

        return true;
    }

    private boolean validateUsernameLenght(String username){
        return username.length()>15;
    }

    private boolean validateUsernameChars(String username){
        return StringUtils.isAlphanumeric(username);
    }


}
