package com.movies.movieslist.image.util;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveResult {
    private boolean error;
    private String filename;
    private String link;
}
