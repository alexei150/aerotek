package com.swecor.aerotek.model.selection.Instalation.builder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InflowOrOutflowToBuilder {
    //Направление стрелки, required. Налево - false, Направо - true
    private boolean direction;
    private List<Integer> sectionTypes;
}
