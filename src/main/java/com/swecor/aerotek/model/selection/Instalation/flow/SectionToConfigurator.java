package com.swecor.aerotek.model.selection.Instalation.flow;

import com.swecor.aerotek.model.library.sectionType.SectionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionToConfigurator {

    //Признак дополнительного клапана
    private boolean airValve;
    //Список клапанов
    private List<AirValves> airValves;
    //Список расчитываемых параметров
    private List<ParameterToSelection> calculated;
    //Список задаваемых параметров
    private List<ParameterToSelection> form;
    private Byte position;
    private SectionType sectionType;
    private int selectedSectionId;
}
