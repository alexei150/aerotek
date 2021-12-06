package com.swecor.aerotek.service.calculation.sectionCalc;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.SelectedHeatExchangerNotFound;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.selection.HeatExchangerSelectionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.swecor.aerotek.service.Constants.FREON_COOLER_ID;

@Service
@Transactional
public class FreonCoolerCalculationService implements Calculated {

    private final HeatExchangerSelectionService heatExchangerSelectionService;

    private final SectionRepository sectionRepository;

    public FreonCoolerCalculationService(@Qualifier("freonCoolerSelectionServiceImpl") HeatExchangerSelectionService heatExchangerSelectionService, SectionRepository sectionRepository) {
        this.heatExchangerSelectionService = heatExchangerSelectionService;
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
                    installation.getInflowConsumption(),
                    installation.getInflowParameters(),
                    installation.getStandardSize());

            //вытяжка
            calculateFlowParameters(
                    installation.getOutflowConsumption(),
                    installation.getOutflowParameters(),
                    installation.getStandardSize());
        } else if (isInflow) {
            //приток
            calculateFlowParameters(
                    installation.getInflowConsumption(),
                    installation.getInflowParameters(),
                    installation.getStandardSize());
        } else if (isOutflow) {
            //вытяжка
            calculateFlowParameters(
                    installation.getOutflowConsumption(),
                    installation.getOutflowParameters(),
                    installation.getStandardSize());
        }

        return installation;
    }

    @Override
    public int getMySectionTypeId() {
        return FREON_COOLER_ID;
    }

    //По standard_size и section type id получаем отсортированный список index теплообменника
    //Перебераем index
    // -> по каждому index, standard_size и section type id вытягиваем из бд входные параметры для dll
    // -> направляем  соедененные входные данные введенные пользователем и полученные из бд, в dll
    // -> если получаем код ответа RECALC_NO_ERROR, то вставляем параметры response dll  в посчитанную секцию теплообменника
    // -> иначе позвращаем на фронт код ответа
    private void calculateFlowParameters(int flowConsumption, List<SectionToConfigurator> flowSections, byte standardSize) {

        for (SectionToConfigurator sectionToConfigurator : flowSections) {
            if (sectionToConfigurator.getSectionType().getId() == FREON_COOLER_ID) {
                List<ParameterToSelection> finishCalculatedParameters = calculateFlow(
                        flowConsumption,
                        sectionToConfigurator,
                        standardSize);

                sectionToConfigurator.setCalculated(finishCalculatedParameters);
                sectionToConfigurator.setSelectedSectionId(Objects.requireNonNull(getSelectedSection(standardSize)).getId());
            }
        }
    }

    private List<ParameterToSelection> calculateFlow(int flowConsumption, SectionToConfigurator sectionToConfigurator, byte standardSize) {

        return heatExchangerSelectionService.selectHeatExchanger(flowConsumption, standardSize, sectionToConfigurator).getCalculated();
    }

    private Section getSelectedSection(byte standardSize) {
        List<Section> selectedSections = sectionRepository.getSelectedSectionByStandardSizeAndSectionTypeId(
                standardSize,
                FREON_COOLER_ID);
        if (!selectedSections.isEmpty()) {
            return selectedSections.get(0);
        } else {
            throw new SelectedHeatExchangerNotFound();
        }
    }
}
