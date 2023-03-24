package com.movies.movieslist.user;

import com.movies.movieslist.config.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
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
}
