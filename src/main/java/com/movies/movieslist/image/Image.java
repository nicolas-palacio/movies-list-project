package com.movies.movieslist.image;


import com.movies.movieslist.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @OneToMany(mappedBy = "image")
    private List<User> users;

}
