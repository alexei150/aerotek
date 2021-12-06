package com.swecor.aerotek.service.calculation.sectionCalc;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.calculation.ParameterByNameNotFound;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.ParameterValueIsNull;
import com.swecor.aerotek.rest.exceptions.selection.SelectedAirValveNotFound;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.calculation.Impl.PressureLossServiceImpl;
import com.swecor.aerotek.service.calculation.PressureLossService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.swecor.aerotek.service.Constants.*;

import java.util.List;

@Service
@Transactional
public class AirValveCalculationService implements Calculated {

    private final SectionRepository sectionRepository;
    private final PressureLossService pressureLossService;

    public AirValveCalculationService(SectionRepository sectionRepository, PressureLossServiceImpl pressureLossService) {
        this.sectionRepository = sectionRepository;
        this.pressureLossService = pressureLossService;
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
        return AIR_VALVE_ID;
    }

    private void calculateFlowParameters(List<SectionToConfigurator> flowParameters, int flowConsumption, byte standardSize) {

        for (SectionToConfigurator sectionToConfigurator : flowParameters) {
            if (sectionToConfigurator.getSectionType().getId() == AIR_VALVE_ID) {

                Section selectedSection = sectionRepository.getSelectedValve(
                        standardSize,
                        AIR_VALVE_ID,
                        getValveSize(sectionToConfigurator),
                        getValveHeaving(sectionToConfigurator),
                        VALVE_SIZE,
                        VALVE_HEAVING);

                if (selectedSection != null) {
                    sectionToConfigurator.setSelectedSectionId(selectedSection.getId());
                } else throw new SelectedAirValveNotFound();

                sectionToConfigurator.setCalculated(
                        setCalculatedParameters(
                                sectionToConfigurator.getCalculated(),
                                selectedSection,
                                flowConsumption));
            }
        }
    }

    private String getValveSize(SectionToConfigurator sectionToConfigurator) {
        return getFormParameterByName(sectionToConfigurator.getForm(), VALVE_SIZE).getValue();
    }

    private String getValveHeaving(SectionToConfigurator sectionToConfigurator) {
        return getFormParameterByName(sectionToConfigurator.getForm(), VALVE_HEAVING).getValue();
    }

    private ParameterToSelection getFormParameterByName(List<ParameterToSelection> formParameters, String parameterName) {
        for (ParameterToSelection parameter : formParameters) {
            if (parameter.getParam().getName().equals(parameterName)) {
                if (parameter.getValue() == null){
                    throw new ParameterValueIsNull("Параметр '" + parameter.getParam().getName() + "' у Воздушного клапана == null");
                }
                return parameter;
            }
        }
        throw new ParameterByNameNotFound(parameterName + "' у Воздушного клапана");
    }

    private List<ParameterToSelection> setCalculatedParameters(List<ParameterToSelection> calculatedParameters, Section selectedSection, int airConsumptionInput) {
        for (ParameterToSelection parameter : calculatedParameters) {
            switch (parameter.getParam().getName()){
                case (WORKING_RANGE) -> parameter.setValue(
                        sectionRepository.getSelectedSectionParameterValue(selectedSection.getId(), WORKING_RANGE));
                case (PRESSURE_LOSS) -> parameter.setValue(
                        String.format("%.2f", (pressureLossService.getPressureLoss(selectedSection, airConsumptionInput))));
            }
        }
        return calculatedParameters;
    }
}
