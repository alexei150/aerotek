package com.swecor.aerotek.model.selection.Instalation.temperature;

import lombok.Data;

@Data
public class ModeTemperature {

    private Double inletTemperature;
    private Double outletTemperature;
    private Double inletHumidity;
    private Double outletHumidity;
}
