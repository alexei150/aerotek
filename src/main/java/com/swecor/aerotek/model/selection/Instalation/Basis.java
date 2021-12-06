package com.swecor.aerotek.model.selection.Instalation;

import com.swecor.aerotek.model.media.MediaContent;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Basis {

    private Long id;
    private String name;
    private String description;
    private MediaContent image;
}
