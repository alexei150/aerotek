package com.swecor.aerotek.persist.media;

import com.swecor.aerotek.model.media.SectionTypeIcon;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionTypeIconRepository extends CrudRepository<SectionTypeIcon, Integer> {
}
