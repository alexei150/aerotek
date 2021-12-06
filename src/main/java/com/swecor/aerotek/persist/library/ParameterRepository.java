package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.parameter.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Integer> {

    List<Parameter> findByName(String name);

    @Query(value = "select pv.value  from parameter_value as pv " +
            "inner join parameter as p on pv.parameter_id = p.id " +
            "where section_id = :sectionId and p.name = :parameterName " +
            "limit 1", nativeQuery = true)
    double getParameter(@Param("sectionId") int sectionId, @Param("parameterName") String parameterName);
}
