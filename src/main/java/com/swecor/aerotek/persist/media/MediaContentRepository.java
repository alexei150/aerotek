package com.swecor.aerotek.persist.media;

import com.swecor.aerotek.model.media.MediaContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaContentRepository extends JpaRepository<MediaContent, Integer> {
}
