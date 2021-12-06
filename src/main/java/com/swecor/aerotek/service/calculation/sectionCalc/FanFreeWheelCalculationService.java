package com.swecor.aerotek.service.calculation.sectionCalc;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggFansResponse;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.calculation.ParameterByNameNotFound;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.ParameterValueIsNull;
import com.swecor.aerotek.rest.exceptions.selection.SelectedFanFreeWellNotFound;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.calculation.FlowPressureLossService;
import com.swecor.aerotek.service.selection.FanSelectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.swecor.aerotek.service.Constants.*;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class FanFreeWheelCalculationService implements Calculated {

    private final FanSelectionService fanSelectionService;

    private final FlowPressureLossService flowPressureLossService;

    private final SectionRepository sectionRepository;

    public FanFreeWheelCalculationService(FanSelectionService fanSelectionService, FlowPressureLossService flowPressureLossService, SectionRepository sectionRepository) {
        this.fanSelectionService = fanSelectionService;
        this.flowPressureLossService = flowPressureLossService;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public InstallationToSelection calculate(InstallationToSelection installation) {

        boolean isInflow = !installation.getInflowParameters().isEmpty();
        boolean isOutflow = !installation.getOutflowParameters().isEmpty();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {
            //приток
            calculateFlowParameters(
                    installation.getInflowParameters(),
                    installation.getInflowConsumption(),
                    installation.getInflowPressure(),
                    installation.getStandardSize());

            //вытяжка
            calculateFlowParameters(
                    installation.getOutflowParameters(),
                    installation.getOutflowConsumption(),
                    installation.getOutflowPressure(),
                    installation.getStandardSize());
        } else if (isInflow) {
            //приток
            calculateFlowParameters(
                    installation.getInflowParameters(),
                    installation.getInflowConsumption(),
                    installation.getInflowPressure(),
                    installation.getStandardSize());
        } else if (isOutflow) {
            //вытяжка
            calculateFlowParameters(
                    installation.getOutflowParameters(),
                    installation.getOutflowConsumption(),
                    installation.getOutflowPressure(),
                    installation.getStandardSize());
        }

        return installation;
    }

    @Override
    public int getMySectionTypeId() {
        return FAN_FREE_WHEEL_ID;
    }

    private void calculateFlowParameters(List<SectionToConfigurator> flowSections, int flowConsumption, double flowPressure, byte standardSize) {

        double flowWorkingPointPressure = flowPressureLossService.getInstallationPressureLoss(flowSections, flowConsumption) + flowPressure;

        for (SectionToConfigurator sectionToConfigurator : flowSections) {

            if (sectionToConfigurator.getSectionType().getId() == FAN_FREE_WHEEL_ID) {
                List<ParameterToSelection> finishCalculatedParameters = calculateFlow(
                        sectionToConfigurator.getCalculated(),
                        flowConsumption,
                        flowPressure,
                        standardSize,
                        flowWorkingPointPressure);

                sectionToConfigurator.setCalculated(finishCalculatedParameters);
                sectionToConfigurator.setSelectedSectionId(Objects.requireNonNull(getSelectedSection(standardSize)).getId());
            }
        }
    }

    private List<ParameterToSelection> calculateFlow(List<ParameterToSelection> calculatedParameters,
                                                     int flowConsumption,
                                                     double flowPressure,
                                                     byte standardSize,
                                                     double installationPressureLoss) {

        ZiehlAbeggFansResponse ziehlAbeggFansResponse = fanSelectionService.selectFan(
                standardSize,
                flowConsumption,
                installationPressureLoss);

        for (ParameterToSelection parameter : calculatedParameters) {
            switch (parameter.getParam().getName()) {
                case ("Потери давления установки") -> parameter.setValue(
                        String.format("%.2f", installationPressureLoss)
                );
                case ("Располагаемый напор") -> parameter.setValue(
                        String.valueOf(flowConsumption)
                );
                case ("Давление в рабочей точке") -> parameter.setValue(
                        String.format("%.2f", installationPressureLoss += flowPressure)
                );
                case ("Эффективность вентилятора") -> parameter.setValue(
                        String.format("%.2f", ziehlAbeggFansResponse.getErpNStat())
                );
                case ("Скорость работы вентилятора"), ("Частота работы двигателя") -> parameter.setValue(
                        ziehlAbeggFansResponse.getNominalSpeed()
                );
                case ("Индекс вентилятора") -> parameter.setValue(
                        ziehlAbeggFansResponse.getType()

                );
                case ("Потребляемая мощность") -> parameter.setValue(
                        String.format("%.2f", ziehlAbeggFansResponse.getZaP1())
                );
                case ("Номинальная мощность") -> parameter.setValue(
                        String.format("%.2f", ziehlAbeggFansResponse.getPowerOutputKw())
                );
                case ("Рабочий ток / Напряжение"), ("Управляющее напряжение") -> parameter.setValue(
                        ziehlAbeggFansResponse.getZaMainsSupply()
                );
                case ("Шумовые характеристики всасывания") -> parameter.setValue(
                        String.format("%.2f", ziehlAbeggFansResponse.getZaLwa5())
                );
                case ("Шумовые характеристики нагнетания") -> parameter.setValue(
                        String.format("%.2f", ziehlAbeggFansResponse.getZaLwa6())
                );
                case ("Марка двигателя") -> parameter.setValue(
                        "Ziehl-Abegg"
                );
                case ("Индекс Двигателя") -> parameter.setValue(
                        "Indefinite"
                );
                case ("SFP для чистых фильтров") -> parameter.setValue(
                        String.format("%.2f", ziehlAbeggFansResponse.getZaSfp())
                );


            }
        }

        return calculatedParameters;

    }

    private Section getSelectedSection(byte standardSize) {
                List<Section> selectedSections = sectionRepository.getSelectedSectionByStandardSizeAndSectionTypeId(
                        standardSize,
                        FAN_FREE_WHEEL_ID);
                if (!selectedSections.isEmpty()) {
                    return selectedSections.get(0);
                } else {
                    throw new SelectedFanFreeWellNotFound();
                }
    }

    //возможно еще пригодится
    private String getFanDirection(SectionToConfigurator sectionToConfigurator) {
        return getFormParameterByName(sectionToConfigurator.getForm()).getValue();
    }

    private ParameterToSelection getFormParameterByName(List<ParameterToSelection> formParameters) {
        for (ParameterToSelection parameter : formParameters) {
            if (parameter.getParam().getName().equals(FAN_AIR_OUTLET_DIRECTION)) {
                if (parameter.getValue() == null){
                    throw new ParameterValueIsNull("Параметр '" + parameter.getParam().getName() + "' у Вентилятора свободное колесо == null");
                }
                return parameter;
            }
        }
        throw new ParameterByNameNotFound(FAN_AIR_OUTLET_DIRECTION + "' у Вентилятора свободное колесо");
    }
}
