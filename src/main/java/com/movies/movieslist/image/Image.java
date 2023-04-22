package com.movies.movieslist.image;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "image")
@Getter
public class Image {
    @Id
    private Long id;
    private String filename;

    private String mimeType;

    private byte[] data;
}
