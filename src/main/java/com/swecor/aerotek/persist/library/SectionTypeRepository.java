package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.sectionType.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectionTypeRepository extends JpaRepository<SectionType, Integer> {

    List<SectionType> findByName(String name);

    List<SectionType> findByCode(String code);
}
