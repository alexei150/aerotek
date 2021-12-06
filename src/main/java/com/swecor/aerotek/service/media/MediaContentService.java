package com.swecor.aerotek.service.media;

import com.swecor.aerotek.model.media.MediaContent;
import com.swecor.aerotek.model.media.MediaContentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaContentService {

    MediaContent createMediaContent (MediaContentDTO mediaContentDTO);

    MediaContent getMediaContent (int id);

    MediaContent updatePath (int id, MultipartFile file) throws IOException;

    void deleteMediaContent(int id);

}
