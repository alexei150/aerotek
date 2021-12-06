package com.swecor.aerotek.model.library.element;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParameterValueForResponse {

    private Integer id;
    private String name;
    private String value;
    private String unit;
}
