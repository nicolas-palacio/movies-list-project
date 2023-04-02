package com.movies.movieslist.auth.util;

import com.movies.movieslist.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class EmailValidator implements Predicate<String> {
    private final UserRepository userRepository;

    @Override
    public boolean test(String email) {
        String regex="\\w@(gmail|outlook).com";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(email);

        if(!userRepository.findByEmail(email).isEmpty()){
            return false;
        }


        if(matcher.find()){
            return true;
        }
        return false;
    }
}
