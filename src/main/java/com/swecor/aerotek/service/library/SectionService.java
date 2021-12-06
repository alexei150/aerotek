package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.library.section.ConstructedSectionResponse;
import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.library.section.SectionRequestDTO;

import java.util.List;

public interface SectionService {

    Section createSection(SectionRequestDTO sectionRequest);

    Section getSection(int id);

    List<Section> showSections();

    Section updateSection(SectionRequestDTO sectionRequest);

    void deleteSection(int id);

    ConstructedSectionResponse getConstructedSection(int id);
}
