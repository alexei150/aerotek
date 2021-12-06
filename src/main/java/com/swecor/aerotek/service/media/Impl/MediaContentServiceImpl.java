package com.swecor.aerotek.service.media.Impl;

import com.swecor.aerotek.model.media.MediaContent;
import com.swecor.aerotek.model.media.MediaContentDTO;
import com.swecor.aerotek.persist.media.MediaContentRepository;
import com.swecor.aerotek.rest.exceptions.media.FileIsEmpty;
import com.swecor.aerotek.rest.exceptions.media.FileIsMissingFromTheHardDisk;
import com.swecor.aerotek.rest.exceptions.media.MediaContentIdIsAbsent;
import com.swecor.aerotek.service.media.MediaContentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class MediaContentServiceImpl implements MediaContentService {

    private final MediaContentRepository mediaContentRepository;

    @Value("${upload.owner}")
    private String owner;

    @Value("${upload.path}")
    private String propertyPath;

    public MediaContentServiceImpl(MediaContentRepository mediaContentRepository) {
        this.mediaContentRepository = mediaContentRepository;
    }

    @Override
    public MediaContent createMediaContent(MediaContentDTO mediaContentDTO) {
        MediaContent mediaContent = new MediaContent().toBuilder().
                name(mediaContentDTO.getName()).
                description(mediaContentDTO.getDescription()).build();

        return mediaContentRepository.save(mediaContent);

    }

    @Override
    public MediaContent getMediaContent(int id) {
        return mediaContentRepository.findById(id).orElseThrow(MediaContentIdIsAbsent::new);
    }

    @Override
    public MediaContent updatePath(int id, MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String uploadPath = Paths.get(propertyPath).toString();
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

//            todo это надо будет добавить если на боевом серваке необходимо изменять владельца файла
//            устанавливаем владельца файла
//                Path path = Paths.get(uploadPath + "/" + resultFilename);
//
//                FileSystem fileSystem = path.getFileSystem();
//
//                UserPrincipalLookupService service = fileSystem.getUserPrincipalLookupService();
//                UserPrincipal userPrincipal = service.lookupPrincipalByName(owner);
//
//                Files.setOwner(path, userPrincipal);

            MediaContent mediaContentDB = mediaContentRepository.findById(id).orElseThrow(MediaContentIdIsAbsent::new);
            mediaContentDB.setPath(resultFilename);

            return mediaContentRepository.save(mediaContentDB);

        } else {
            throw new FileIsEmpty();
        }
    }

    @Override
    public void deleteMediaContent(int id) {
        MediaContent mediaContentDB = mediaContentRepository.findById(id).orElseThrow(MediaContentIdIsAbsent::new);

        File fileOnHardDisk = new File(propertyPath + "/" + mediaContentDB.getPath());

        if (!fileOnHardDisk.delete()) {
            throw new FileIsMissingFromTheHardDisk();
        }
        mediaContentRepository.deleteById(id);
    }
}
