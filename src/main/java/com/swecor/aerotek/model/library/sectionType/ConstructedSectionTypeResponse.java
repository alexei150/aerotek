package com.swecor.aerotek.model.library.sectionType;

import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.media.MediaContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ConstructedSectionTypeResponse {

    private int id;
    private String name;
    private String code;
    private String note;
    @Builder.Default
    private Boolean twoLevel = false;
    @Builder.Default
    private Boolean outflow = true;
    @Builder.Default
    private Boolean inflow = true;
    private MediaContent image;
    private MediaContent iconBase;
    private MediaContent iconInflowToRight;
    private MediaContent iconInflowToLeft;
    private MediaContent iconOutflowToRight;
    private MediaContent iconOutflowToLeft;
    private List<Parameter> parameters;
}
