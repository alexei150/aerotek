package com.swecor.aerotek.model.selection.Instalation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AcousticPerformanceDTO {

    private String hz63;
    private String hz125;
    private String hz250;
    private String hz500;
    private String hz1000;
    private String hz2000;
    private String hz4000;
    private String hz8000;
    private String sum;
}
