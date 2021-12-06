package com.swecor.aerotek.model.selection.fanSelect;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZiehlAbeggFansRequest {

    private String username = "ZAFS36823";
    private String password = "kqv2e2";
    @JsonProperty("insert_nominal_values")
    private boolean insertNominalValues = true;
    @JsonProperty("SESSIONID")
    private String sessionId;
    private String cmd = "search";
    @JsonProperty("cmd_param")
    private String cmdParam = "0";
    private int qv;
    private double psf;
    @JsonProperty("spec_products")
    private String specProducts = "PF_00";
    @JsonProperty("product_range")
    private String productRange = "BR_08|BR_09";
    @JsonProperty("current_phase")
    private Integer currentPhase = 3;
    private Integer voltage = 400;
    @JsonProperty("nominal_frequency")
    private Integer nominalFrequency = 50;
    @JsonProperty("search_tolerance")
    private Integer searchTolerance = 10;
    @JsonProperty("fan_type")
    private String fanType;
    @JsonProperty("motor_technology")
    private String motorTechnology = "ZAmotpremium IE3";
    private String language = "RU";

}
