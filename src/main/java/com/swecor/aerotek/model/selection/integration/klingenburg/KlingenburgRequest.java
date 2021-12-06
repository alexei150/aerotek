package com.swecor.aerotek.model.selection.integration.klingenburg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class KlingenburgRequest {

    private double airPressure;
    private double coldVolumeIn;
    private double coldTempIn;
    private double coldHumidityIn;
    private double warmVolumeIn;
    private double warmTempIn;
    private double warmHumidityIn;
    private Character rotorType;
    private String waveHeight;
    private Integer outerDiameter;
}
