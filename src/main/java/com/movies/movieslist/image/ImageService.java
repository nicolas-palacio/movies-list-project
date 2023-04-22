package com.movies.movieslist.image;


import com.movies.movieslist.security.exceptions.NotFoundException;
import com.movies.movieslist.user.User;
import com.movies.movieslist.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    public Image getImage(String filename){
        return imageRepository.findByFilename(filename).orElseThrow(() ->
                new NotFoundException("Image not found."));
    }

    public Image save(MultipartFile file) throws Exception{
        if(imageRepository.existsByFilename(file.getOriginalFilename())){
                log.info("Image {} have already existed",file.getOriginalFilename());
        }

        var image=Image.builder().filename(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .data(file.getBytes())
                .build();

        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user=userRepository.findByEmail(email);
        user.get().setImage(image);

        return imageRepository.save(image);
    }

}
