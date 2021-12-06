package com.swecor.aerotek.model.selection.Instalation.builder;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InflowToBuilder {
    //Направление стрелки, required. Налево - false, Направо - true
    private boolean direction;
    private List<Integer> sectionTypes;
}
