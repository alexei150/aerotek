package com.swecor.aerotek.model.selection.Instalation.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstallationBuilderDTO {

    private InflowOrOutflowToBuilder inflow;
    private InflowOrOutflowToBuilder outflow;
    //Расположение вытяжки, required: false - снизу под притоком, true - сверху над притоком
    private boolean outflowIsUp;
}
