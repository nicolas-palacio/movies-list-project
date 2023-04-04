package com.movies.movieslist.config.exceptions;

public class ForbiddenException extends RuntimeException{
    private static final String DESCRIPTION= "Forbidden (403)";

    public ForbiddenException(String detail){
        super(DESCRIPTION + "."+ detail);
    }
}
