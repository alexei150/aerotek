package com.swecor.aerotek.persist.library;

import com.swecor.aerotek.model.library.section.ElectricHeater;
import com.swecor.aerotek.model.library.section.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    List<Section> findByDrawingCode(String drawingCode);

    //Клапан
    @Query(value = "SELECT * FROM section WHERE id =(" +
            "SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT s.id FROM section AS s " +
            "WHERE s.standard_size = :standardSize AND s.section_type_id = :sectionTypeId) " +
            "AND (p.name = :valveSize AND pv.value = :size)) " +
            "AND (p.name = :valveHeating AND pv.value = :heating) " +
            "LIMIT 1)", nativeQuery = true)
    Section getSelectedValve(@Param("standardSize") byte standardSize,
                             @Param("sectionTypeId") int sectionTypeId,
                             @Param("size") String size,
                             @Param("heating") String heating,
                             @Param("valveSize") String valveSize,
                             @Param("valveHeating") String valveHeating);

    //Шумоглушитель
    @Query(value = "SELECT * FROM section WHERE id =(" +
            "SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT s.id FROM section AS s " +
            "WHERE s.standard_size = :standardSize AND s.section_type_id = :sectionTypeId) " +
            "AND (p.name = :sectionLength AND pv.value = :mufflerSize)) " +
            "LIMIT 1)", nativeQuery = true)
    Section getSelectedMuffler(@Param("standardSize") byte standardSize,
                               @Param("sectionTypeId") int sectionTypeId,
                               @Param("mufflerSize") String mufflerSize,
                               @Param("sectionLength") String sectionLength);

    //Пустая секция
    @Query(value = "SELECT * FROM section WHERE id =(" +
            "SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT s.id FROM section AS s " +
            "WHERE s.standard_size = :standardSize AND s.section_type_id = :sectionTypeId) " +
            "AND (p.name = :sectionLength AND pv.value = :emptySectionSize)) " +
            "LIMIT 1)", nativeQuery = true)
    Section getSelectedEmptySection(@Param("standardSize") byte standardSize,
                                    @Param("sectionTypeId") int sectionTypeId,
                                    @Param("emptySectionSize") String emptySectionSize,
                                    @Param("sectionLength") String sectionLength);

    @Query(value = "SELECT value FROM section AS s " +
            "INNER JOIN parameter_value AS pv ON s.id = pv.section_id " +
            "INNER JOIN parameter AS p ON pv.parameter_id = p.id " +
            "WHERE s.id = :sectionId AND p.name = :paramName", nativeQuery = true)
    String getSelectedSectionParameterValue(@Param("sectionId") int sectionId,
                                            @Param("paramName") String paramName);

    //Вентилятор свободное колесо
    //алгоритм подбора изменен
//    @Query(value = "select * " +
//            "from section " +
//            "where id in " +
//            "   (select section_id " +
//            "   from section_element as se " +
//            "   inner join element as e on e.id = se.element_id " +
//            "   where se.section_id in " +
//            "       (SELECT DISTINCT section_id " +
//            "       FROM parameter_value AS pv " +
//            "       INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
//            "       WHERE section_id IN " +
//            "           (SELECT s.id " +
//            "           FROM section AS s " +
//            "           WHERE s.standard_size = :standardSize " +
//            "           AND s.section_type_id = :sectionTypeId) " +
//            "       AND (p.name = :outletDirection AND pv.value = :direction)) " +
//            "   and e.index = :index " +
//            "   limit 1)", nativeQuery = true)
    @Query(value = "select *\n" +
            "from section\n" +
            "where id in\n" +
            "      (select section_id\n" +
            "       from section as s\n" +
            "                inner join variable_element as ve on ve.section_id = s.id\n" +
            "       where s.standard_size = :standardSize\n" +
            "       and s.section_type_id = :sectionTypeId\n" +
            "       limit 1);", nativeQuery = true)
    List<Section> getSelectedSectionByStandardSizeAndSectionTypeId(@Param("standardSize") byte standardSize,
                                                                   @Param("sectionTypeId") int sectionTypeId);

    //Карманный фильтр
    @Query(value = "SELECT * FROM section WHERE id =(" +
            "SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT s.id FROM section AS s " +
            "WHERE s.standard_size = :standardSize AND s.section_type_id = :sectionTypeId) " +
            "AND (p.name = :filterClassNameToForm AND pv.value = :filterClass)) " +
            "LIMIT 1)", nativeQuery = true)
    Section getPocketFilter(@Param("standardSize") byte standardSize,
                            @Param("sectionTypeId") int sectionTypeId,
                            @Param("filterClass") String filterClass,
                            @Param("filterClassNameToForm") String filterClassNameToForm);

    //Кассетный фильтр
    @Query(value = "SELECT * FROM section WHERE id =(" +
            "SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT DISTINCT section_id FROM parameter_value AS pv " +
            "INNER JOIN parameter AS p ON p.id = pv.parameter_id " +
            "WHERE section_id IN" +
            "(SELECT s.id FROM section AS s " +
            "WHERE s.standard_size = :standardSize AND s.section_type_id = :sectionTypeId) " +
            "AND (p.name = :filterClassNameToForm AND pv.value = :filterClass)) " +
            "LIMIT 1)", nativeQuery = true)
    Section getCassetteFilter(@Param("standardSize") byte standardSize,
                            @Param("sectionTypeId") int sectionTypeId,
                            @Param("filterClass") String filterClass,
                            @Param("filterClassNameToForm") String filterClassNameToForm);

    //Электрический нагреватель по тепловой мощности
    @Query(value = "SELECT DISTINCT section_id as id, pv.value as value\n" +
            "FROM parameter_value AS pv\n" +
            "         INNER JOIN parameter AS p ON p.id = pv.parameter_id\n" +
            "WHERE section_id IN\n" +
            "      (SELECT section_id\n" +
            "       FROM parameter_value AS pv\n" +
            "                INNER JOIN parameter AS p ON p.id = pv.parameter_id\n" +
            "       WHERE section_id IN\n" +
            "             (SELECT s.id\n" +
            "              FROM section AS s\n" +
            "              WHERE s.standard_size = :standardSize\n" +
            "                AND s.section_type_id = :sectionTypeId)\n" +
            "         AND p.name = :numberOfControlStepsName\n" +
            "         AND pv.value = :numberOfControlStepsValue)\n" +
            "  AND p.name = :heatPowerName\n" +
            "  AND try_cast_int(pv.value) >= :heatPowerValue\n" +
            "ORDER BY value ASC\n" +
            "LIMIT 1;", nativeQuery = true)
    ElectricHeater getElectricHeater (@Param("standardSize") byte standardSize,
                                      @Param("sectionTypeId") int sectionTypeId,
                                      @Param("numberOfControlStepsName") String numberOfControlStepsName,
                                      @Param("numberOfControlStepsValue") String numberOfControlStepsValue,
                                      @Param("heatPowerName") String heatPowerName,
                                      @Param("heatPowerValue") int heatPowerValue);

}
