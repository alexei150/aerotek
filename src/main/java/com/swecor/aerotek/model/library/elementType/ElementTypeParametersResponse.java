package com.swecor.aerotek.model.library.elementType;

import com.swecor.aerotek.model.library.parameter.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ElementTypeParametersResponse {

    private int id;
    private String name;
    private String note;
    private List<Parameter> parameters;

}
