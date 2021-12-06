package com.swecor.aerotek.model.selection.fanSelect;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZiehlAbeggSessionIdResponse {

    @JsonProperty("SESSIONID")
    private String sessionId;
}
