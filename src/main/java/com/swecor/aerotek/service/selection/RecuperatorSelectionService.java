package com.swecor.aerotek.service.selection;

import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;

import java.util.List;

public interface RecuperatorSelectionService {

    List<SectionToConfigurator> selectRecuperator(int flowConsumption, int outflowConsumption, byte standardSize, SectionToConfigurator inflowRecuperator, SectionToConfigurator outflowRecuperator);
}
