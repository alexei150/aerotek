package com.swecor.aerotek.model.library.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElementParametersValuesResponse {

    private int id;
    private String name;
    private String brand;
    private String code;
    private String index;
    private Float costPrice;
    private String description;
    private String note;
    private String drawingCode;
    private Integer elementTypeId;
    private String elementTypeName;
    private List<ParameterValueForResponse> parametersValues;
}
