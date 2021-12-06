package com.swecor.aerotek.model.selection.Instalation;

import com.swecor.aerotek.model.selection.Instalation.temperature.FlowTemperature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirCharacteristicDTO {

    private String name;
    private String address;
    private Integer inflowPressure;
    private Integer outflowPressure;
    private Integer inflowConsumption;
    private Integer outflowConsumption;
    private FlowTemperature inflowTemperature;
    private FlowTemperature outflowTemperature;
    private byte standardSize;
    private String description;
    private Integer thickness;
    private boolean outside;
    private boolean summerMode;
    private boolean winterMode;
}
