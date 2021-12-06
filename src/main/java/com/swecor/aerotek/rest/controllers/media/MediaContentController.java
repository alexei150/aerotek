package com.swecor.aerotek.rest.controllers.media;

import java.io.IOException;

import com.swecor.aerotek.model.media.MediaContentDTO;
import com.swecor.aerotek.service.media.MediaContentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/media/content")
public class MediaContentController {

    private final MediaContentService mediaContentService;

    public MediaContentController(@Qualifier("mediaContentServiceImpl") MediaContentService mediaContentService) {
        this.mediaContentService = mediaContentService;
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ResponseEntity<?> createMediaContent(@RequestBody MediaContentDTO mediaContentDTO) {

        return ResponseEntity.ok(mediaContentService.createMediaContent(mediaContentDTO));
    }

    @GetMapping(value = "/get/{id}")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ResponseEntity<?> getMediaContent(@PathVariable int id){
        return ResponseEntity.ok(mediaContentService.getMediaContent(id));
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteMediaContent(@PathVariable int id){
        mediaContentService.deleteMediaContent(id);
    }


    @PostMapping(value = "/update-path/{id}")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ResponseEntity<?> updateMediaContentPath(@PathVariable(value = "id") Integer id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(mediaContentService.updatePath(id, file));
    }
}