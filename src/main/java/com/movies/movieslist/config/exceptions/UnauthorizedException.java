package com.movies.movieslist.config.exceptions;

public class UnauthorizedException extends RuntimeException{
    private static final String DESCRIPTION= "Unauthorized (401)";

    public UnauthorizedException(String detail){
        super(DESCRIPTION + "."+ detail);
    }
}
