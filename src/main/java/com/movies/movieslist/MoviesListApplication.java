package com.movies.movieslist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;


@SpringBootApplication
public class MoviesListApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesListApplication.class, args);
	}

}
