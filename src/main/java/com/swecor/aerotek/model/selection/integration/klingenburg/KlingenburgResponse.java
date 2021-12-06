package com.swecor.aerotek.model.selection.integration.klingenburg;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class KlingenburgResponse {

    private int errorCode;
    private String errorMessage;

    protected BigDecimal coldTemperatureOut;
    protected BigDecimal coldHumidity;
    protected BigDecimal coldPressureOut;
    protected BigDecimal coldAreaVelocity;
    protected BigDecimal coldPower;

    protected BigDecimal warmTemperatureOut;
    protected BigDecimal warmHumidity;
    protected BigDecimal warmPressureOut;
    protected BigDecimal warmAreaVelocity;
    protected BigDecimal warmPower;

    protected BigDecimal efficiency;

}
