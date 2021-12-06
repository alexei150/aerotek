package com.swecor.aerotek.model.selection.integration.heaterExchanger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RoenEstRequest {

    //вводит пользователь
    private int mode;
    private double airFlowRate;
    private double airInletTemperature;
    private double airInletHumidity;
    private int fluid;
    private double fluidSpecification;
    private double fluidInletTemperature;
    private double fluidOutletTemperature;
    private double tempEvaporationOrCondensation;

    //берется из бд
    private int geometry;
    private double length;
    private double height;
    private double numRows;
    private int tubesType;
    private int finSpacing;
    private int finType;
    private int circuitsType;
    private double numCircuits;
    private int headerConfiguration;
    private int headerValue1;
    private int headerValue2;
}
