package com.swecor.aerotek.service.calculation;

import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;

import java.util.List;

public interface FlowPressureLossService {

    double getInstallationPressureLoss(List<SectionToConfigurator> sectionsToConfigurator, int flowConsumptionInput);
}
