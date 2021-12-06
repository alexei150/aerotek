package com.swecor.aerotek.service.calculation.Impl;

import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.library.SectionIsAbsent;
import com.swecor.aerotek.service.calculation.FlowPressureLossService;
import com.swecor.aerotek.service.calculation.PressureLossService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowPressureLossServiceImpl implements FlowPressureLossService {

    private final PressureLossService pressureLossService;

    private final SectionRepository sectionRepository;

    public FlowPressureLossServiceImpl(PressureLossService pressureLossService, SectionRepository sectionRepository) {
        this.pressureLossService = pressureLossService;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public double getInstallationPressureLoss(List<SectionToConfigurator> sectionsToConfigurator, int flowConsumptionInput) {

        double flowPressureLoss = 0;

        for (SectionToConfigurator section : sectionsToConfigurator) {
            if (section.getSelectedSectionId() != 0) {
                flowPressureLoss += pressureLossService.getPressureLoss(sectionRepository.findById(section.getSelectedSectionId()).orElseThrow(SectionIsAbsent::new), flowConsumptionInput);
            }
        }
        return  flowPressureLoss;
    }
}
