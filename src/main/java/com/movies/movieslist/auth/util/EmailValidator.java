package com.movies.movieslist.auth.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String s) {
        String regex="\\w@(gmail|outlook).com";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(s);

        if(matcher.find()){
            return true;
        }
        return false;
    }
}
