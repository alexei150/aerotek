package com.swecor.aerotek.model.selection.Instalation.temperature;

import lombok.Data;

@Data
public class FlowTemperature {

    private ModeTemperature winterModeTemperature;
    private ModeTemperature summerModeTemperature;
}
