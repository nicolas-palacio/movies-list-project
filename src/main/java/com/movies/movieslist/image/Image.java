package com.movies.movieslist.image;


import com.movies.movieslist.user.User;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String filename;

    private String mimeType;

    private byte[] data;
    @OneToOne(mappedBy = "image")
    private User user;

}
