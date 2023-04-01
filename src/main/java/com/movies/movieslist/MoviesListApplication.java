package com.movies.movieslist;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Movie List - API", version = "1.0.0"))
@SecurityScheme(name = "BearerJWT",type= SecuritySchemeType.HTTP,scheme = "bearer",bearerFormat ="JWT",description = "JWT Security.")
public class MoviesListApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesListApplication.class, args);
	}

}
