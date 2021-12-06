package com.swecor.aerotek.model.selection.Instalation.flow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParameterToSelection implements Comparable<ParameterToSelection>{

    private DefaultParameter param;
    private String value;

    @Override
    public int compareTo(ParameterToSelection parameterToSelection) {
        return this.param.getId() - parameterToSelection.param.getId();
    }
}
