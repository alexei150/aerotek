package com.swecor.aerotek.model.selection.fanSelect;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZiehlAbeggSessionIdRequest {

    private String username = "ZAFS36823";
    private String password = "kqv2e2";
    @JsonProperty("insert_nominal_values")
    private boolean insertNominalValues = true;
}
