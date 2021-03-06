package com.swecor.aerotek.service.calculation.sectionCalc;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.calculation.ParameterByNameNotFound;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.ParameterValueIsNull;
import com.swecor.aerotek.rest.exceptions.selection.SelectedEmptySectionNotFound;
import com.swecor.aerotek.service.calculation.AirSpeedService;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.calculation.FlowPressureLossService;
import com.swecor.aerotek.service.calculation.PressureLossService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.swecor.aerotek.service.Constants.*;

import java.util.List;

@Service
@Transactional
public class EmptySectionCalculationService implements Calculated {

    private final AirSpeedService airSpeedService;

    private final PressureLossService pressureLossService;

    private final SectionRepository sectionRepository;

    public EmptySectionCalculationService(AirSpeedService airSpeedService, FlowPressureLossService pressureLossService, PressureLossService pressureLossService1, SectionRepository sectionRepository) {
        this.airSpeedService = airSpeedService;
        this.pressureLossService = pressureLossService1;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public InstallationToSelection calculate(InstallationToSelection installationToSelection) {

        boolean isInflow = !installationToSelection.getInflowParameters().isEmpty();
        boolean isOutflow = !installationToSelection.getOutflowParameters().isEmpty();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {
            //????????????
            calculateFlowParameters(
                    installationToSelection.getInflowParameters(),
                    installationToSelection.getInflowConsumption(),
                    installationToSelection.getStandardSize());

            //??????????????
            calculateFlowParameters(
                    installationToSelection.getOutflowParameters(),
                    installationToSelection.getOutflowConsumption(),
                    installationToSelection.getStandardSize());
        } else if (isInflow) {
            //????????????
            calculateFlowParameters(
                    installationToSelection.getInflowParameters(),
                    installationToSelection.getInflowConsumption(),
                    installationToSelection.getStandardSize());
        } else if (isOutflow) {
            //??????????????
            calculateFlowParameters(
                    installationToSelection.getOutflowParameters(),
                    installationToSelection.getOutflowConsumption(),
                    installationToSelection.getStandardSize());
        }

        return installationToSelection;
    }

    @Override
    public int getMySectionTypeId() {
        return EMPTY_SECTION_ID;
    }

    private void calculateFlowParameters(List<SectionToConfigurator> flowParameters, int flowConsumption, byte standardSize) {

        for (SectionToConfigurator sectionToConfigurator : flowParameters) {
            if (sectionToConfigurator.getSectionType().getId() == EMPTY_SECTION_ID) {

                Section selectedSection = sectionRepository.getSelectedEmptySection(standardSize, EMPTY_SECTION_ID, getEmptySectionSize(sectionToConfigurator), SECTION_LENGTH);

                if (selectedSection != null) {
                    sectionToConfigurator.setSelectedSectionId(selectedSection.getId());
                } else throw new SelectedEmptySectionNotFound();

                sectionToConfigurator.setCalculated(
                        setCalculatedParameters(sectionToConfigurator.getCalculated(),
                                selectedSection,
                                flowConsumption));
            }
        }
    }

    private List<ParameterToSelection> setCalculatedParameters(List<ParameterToSelection> calculatedParameters, Section selectedSection, int airConsumption) {

        for (ParameterToSelection parameter : calculatedParameters) {
            switch (parameter.getParam().getName()) {
                case (AIR_SPEED) -> parameter.setValue(String.format("%.2f", airSpeedService.getAirSpeed(selectedSection, airConsumption)));
                case (PRESSURE_LOSS) -> parameter.setValue(String.format("%.2f", (pressureLossService.getPressureLoss(selectedSection, airConsumption))));
            }
        }
        return calculatedParameters;
    }

    private String getEmptySectionSize(SectionToConfigurator sectionToConfigurator) {
        return getFormParameterByName(sectionToConfigurator.getForm()).getValue();
    }

    private ParameterToSelection getFormParameterByName(List<ParameterToSelection> formParameters) {
        for (ParameterToSelection parameter : formParameters) {
            if (parameter.getParam().getName().equals(SECTION_LENGTH)) {
                if (parameter.getValue() == null){
                    throw new ParameterValueIsNull("???????????????? '" + parameter.getParam().getName() + "' ?? ???????????? ???????????? == null");
                }
                return parameter;
            }
        }
        throw new ParameterByNameNotFound(SECTION_LENGTH + "' ?? ???????????? ????????????");
    }
}
