package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.parameterValue.ParameterValue;
import com.swecor.aerotek.model.library.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterValueRepository extends JpaRepository<ParameterValue, Integer> {

    @Modifying
    @Query("delete FROM ParameterValue pv where pv.parameter =?1 and pv.element = ?2")
    void deleteByParameterAndElement (Parameter parameter, Element element);

    @Modifying
    @Query("delete FROM ParameterValue pv where pv.parameter =?1 and pv.section = ?2")
    void deleteByParameterAndSection (Parameter parameter, Section section);
}
