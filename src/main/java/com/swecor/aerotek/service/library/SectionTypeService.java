package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.library.sectionType.SectionType;
import com.swecor.aerotek.model.library.sectionType.ConstructedSectionTypeResponse;
import com.swecor.aerotek.model.library.sectionType.SectionTypeRequestDTO;

import java.util.List;

public interface SectionTypeService {

    SectionType creatSectionType (SectionTypeRequestDTO sectionTypeRequestDTO);

    SectionType getSectionType (int id);

    List<SectionType> showSectionTypes();

    SectionType updateSectionType (SectionTypeRequestDTO sectionTypeRequestDTO);

    void deleteSectionType (int id);

    ConstructedSectionTypeResponse getConstructedSectionType(int id);

    List<ConstructedSectionTypeResponse> getAllConstructedSectionTypes();
}
