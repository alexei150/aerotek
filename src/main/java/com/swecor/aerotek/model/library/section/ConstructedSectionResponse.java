package com.swecor.aerotek.model.library.section;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.swecor.aerotek.model.library.element.ElementCountResponse;
import com.swecor.aerotek.model.library.element.ParameterValueForResponse;
import com.swecor.aerotek.model.library.sectionType.SectionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ConstructedSectionResponse {

    private Integer id;
    private String name;
    private String drawingCode;
    private String description;
    private String note;
    private Float buildCoefficient;
    private Float hardwareCoefficient;
    private Float sectionArea;
    private Byte standardSize;
    private SectionType sectionType;
    private List<ParameterValueForResponse> parametersValues;
    private List<ElementCountResponse> elements;
    private AcousticPerformance acousticPerformance;
    private Map<Integer, Float> airConsumptions;
    private Map<Integer, String> variableElements;

    @JsonGetter("sectionType")
    public String getTheSectionType(){
        return sectionType.getName();
    }

    @JsonGetter("sectionTypeCode")
    public String getTheSectionTypeCode(){
        return sectionType.getCode();
    }

    @JsonGetter("sectionTypeId")
    public Integer getTheSectionTypeId (){
        return sectionType.getId();
    }
}
