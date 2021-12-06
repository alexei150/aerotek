package com.swecor.aerotek.model.selection.Instalation.flow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AirValves {

    private String direction;
    private boolean outside;
    private String size;
    private String type;
}
