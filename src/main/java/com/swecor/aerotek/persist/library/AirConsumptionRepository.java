package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.section.AirConsumption;
import com.swecor.aerotek.model.library.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AirConsumptionRepository extends JpaRepository<AirConsumption, Integer> {

    void deleteBySection(Section section);

    AirConsumption findBySectionAndConsumption(Section section, int airConsumptionInput);

    @Query(value = "SELECT * FROM air_consumption WHERE air_consumption.section_id = :sectionId and air_consumption.consumption > :airConsumptionInput " +
            "ORDER BY air_consumption.consumption ASC " +
            "LIMIT 1", nativeQuery = true)
    AirConsumption getNearestGreater(@Param("sectionId") int sectionId, @Param("airConsumptionInput") int airConsumptionInput);

    @Query(value = "SELECT * FROM air_consumption WHERE air_consumption.section_id = :sectionId and air_consumption.consumption < :airConsumptionInput " +
            "ORDER BY air_consumption.consumption DESC " +
            "LIMIT 1", nativeQuery = true)
    AirConsumption getNearestSmaller(@Param("sectionId") int sectionId, @Param("airConsumptionInput") int airConsumptionInput);
}
