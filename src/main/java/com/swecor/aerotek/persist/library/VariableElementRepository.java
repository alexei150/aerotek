package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.library.section.VariableElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariableElementRepository extends JpaRepository<VariableElement, Integer> {

    void deleteBySection(Section section);

    List<VariableElement> getByStandardSizeAndSectionTypeIdOrderByOrderKeyAsc(Byte standardSize, Integer sectionTypeId);
}
