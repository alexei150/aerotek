package com.swecor.aerotek.service.calculation.sectionCalc;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.calculation.ParameterByNameNotFound;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.ParameterValueIsNull;
import com.swecor.aerotek.rest.exceptions.selection.SelectedMufflerNotFound;
import com.swecor.aerotek.service.calculation.AirSpeedService;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.calculation.PressureLossService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.swecor.aerotek.service.Constants.*;

import java.util.List;

@Service
@Transactional
public class MufflerCalculationService implements Calculated {

    private final SectionRepository sectionRepository;
    private final PressureLossService pressureLossService;
    private final AirSpeedService airSpeedService;

    public MufflerCalculationService(SectionRepository sectionRepository, PressureLossService pressureLossService, AirSpeedService airSpeedService) {
        this.sectionRepository = sectionRepository;
        this.pressureLossService = pressureLossService;
        this.airSpeedService = airSpeedService;
    }

    @Override
    public InstallationToSelection calculate(InstallationToSelection installationToSelection) {

        boolean isInflow = !installationToSelection.getInflowParameters().isEmpty();
        boolean isOutflow = !installationToSelection.getOutflowParameters().isEmpty();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {
            //приток
            calculateFlowParameters(
                    installationToSelection.getInflowParameters(),
                    installationToSelection.getInflowConsumption(),
                    installationToSelection.getStandardSize());

            //вытяжка
            calculateFlowParameters(
                    installationToSelection.getOutflowParameters(),
                    installationToSelection.getOutflowConsumption(),
                    installationToSelection.getStandardSize());
        } else if (isInflow) {
            //приток
            calculateFlowParameters(
                    installationToSelection.getInflowParameters(),
                    installationToSelection.getInflowConsumption(),
                    installationToSelection.getStandardSize());
        } else if (isOutflow) {
            //вытяжка
            calculateFlowParameters(
                    installationToSelection.getOutflowParameters(),
                    installationToSelection.getOutflowConsumption(),
                    installationToSelection.getStandardSize());
        }
        return installationToSelection;
    }

    @Override
    public int getMySectionTypeId() {
        return MUFFLER_ID;
    }

    private void calculateFlowParameters(List<SectionToConfigurator> flowParameters, int flowConsumption, byte standardSize) {

        for (SectionToConfigurator sectionToConfigurator : flowParameters) {
            if (sectionToConfigurator.getSectionType().getId() == MUFFLER_ID) {

                Section selectedSection = sectionRepository.getSelectedMuffler(
                        standardSize,
                        MUFFLER_ID,
                        getMufflerSize(sectionToConfigurator),
                        SECTION_LENGTH);

                if (selectedSection != null) {
                    sectionToConfigurator.setSelectedSectionId(selectedSection.getId());
                } else throw new SelectedMufflerNotFound();

                sectionToConfigurator.setCalculated(
                        setCalculatedParameters(sectionToConfigurator.getCalculated(),
                                selectedSection,
                                flowConsumption));
            }
        }
    }

    private String getMufflerSize(SectionToConfigurator sectionToConfigurator) {
        return getFormParameterByName(sectionToConfigurator.getForm()).getValue();
    }

    private ParameterToSelection getFormParameterByName(List<ParameterToSelection> formParameters) {
        for (ParameterToSelection parameter : formParameters) {
            if (parameter.getParam().getName().equals(SECTION_LENGTH)) {
                if (parameter.getValue() == null){
                    throw new ParameterValueIsNull("Параметр '" + parameter.getParam().getName() + "' у Карманного фильтра == null");
                }
                return parameter;
            }
        }
        throw new ParameterByNameNotFound(SECTION_LENGTH + "' у Шумоглушителя");
    }

    private List<ParameterToSelection> setCalculatedParameters(List<ParameterToSelection> calculatedParameters, Section selectedSection, int airConsumptionInput) {
        for (ParameterToSelection parameter : calculatedParameters) {
            switch (parameter.getParam().getName()) {
                case (NOISE_REDUCTION_LEVEL) -> parameter.setValue(
                        sectionRepository.getSelectedSectionParameterValue(selectedSection.getId(), NOISE_REDUCTION_LEVEL));
                case (AIR_SPEED) -> parameter.setValue(
                        String.format("%.2f", (airSpeedService.getAirSpeed(selectedSection, airConsumptionInput))));
                case (PRESSURE_LOSS) -> parameter.setValue(
                        String.format("%.2f", (pressureLossService.getPressureLoss(selectedSection, airConsumptionInput))));
            }
        }
        return calculatedParameters;
    }
}
