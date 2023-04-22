package com.movies.movieslist.image;

import com.movies.movieslist.image.util.SaveResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/user/image")
@RequiredArgsConstructor
@Log4j2
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Post the user's profile picture.",tags = {"User"})
    @PostMapping("/movie")
    @ResponseBody
    public SaveResult upload(@RequestPart MultipartFile file){
        try{
            var image= imageService.save(file);
            return SaveResult.builder()
                    .error(false)
                    .filename(image.getFilename())
                    .link(crateImageLink(image.getFilename()))
                    .build();
            
        }catch(Exception e){
            log.error("Failed to save image",e);
            return SaveResult.builder().error(true).filename(file.getOriginalFilename()).build();
            
        }
        
    }

    private String crateImageLink(String filename) {
        return ServletUriComponentsBuilder.fromCurrentRequest().replacePath("/images/db"+filename).toUriString();
    }
}
