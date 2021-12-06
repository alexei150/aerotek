package com.swecor.aerotek.model.selection.Instalation.builder;

import com.swecor.aerotek.model.library.sectionType.SectionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionTypeToBuilder {

    private Byte position;
    private SectionType sectionType;
}
