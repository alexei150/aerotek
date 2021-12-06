package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.library.section.SectionElementsCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionElementsCountRepository extends JpaRepository <SectionElementsCount, Integer> {

    void deleteBySection(Section section);

    List<SectionElementsCount> getBySection(Section section);
}
