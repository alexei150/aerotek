package com.swecor.aerotek.model.selection.integration.heaterExchanger;

import lombok.Data;

@Data
public class RoenEstResponse {

    private String status;
    private String code;
    private double airVelocity;
    private double airPressureDrop;
    private double airOutletTemperature;
    private double airOutletHumidity;
    private double nominalCapacity;
    private double fluidFlowRate;
    private double fluidVelocity;
    private double fluidPressureDrop;
    private double fluidOutletTemperature;
}
