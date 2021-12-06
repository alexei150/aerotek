package com.swecor.aerotek.service.calculation.sectionCalc;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.selection.SelectedHeatExchangerNotFound;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.selection.Impl.RotorRecuperatorSelectionServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.swecor.aerotek.service.Constants.ROTOR_RECUPERATOR_ID;

@Service
@Transactional
public class RotorRecuperatorCalculationService implements Calculated {

    private final RotorRecuperatorSelectionServiceImpl rotorRecuperatorSelectionService;

    private final SectionRepository sectionRepository;

    public RotorRecuperatorCalculationService(@Qualifier("rotorRecuperatorSelectionServiceImpl") RotorRecuperatorSelectionServiceImpl rotorRecuperatorSelectionService, SectionRepository sectionRepository) {
        this.rotorRecuperatorSelectionService = rotorRecuperatorSelectionService;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public InstallationToSelection calculate(InstallationToSelection installation) {

        calculateInflowParameters(
                installation.getInflowConsumption(),
                installation.getOutflowConsumption(),
                installation.getInflowParameters(),
                installation.getOutflowParameters(),
                installation.getStandardSize());

        return installation;
    }

    @Override
    public int getMySectionTypeId() {
        return ROTOR_RECUPERATOR_ID;
    }

    private void calculateInflowParameters(int inflowConsumption, int outflowConsumption, List<SectionToConfigurator> inflowSections,
                                           List<SectionToConfigurator> outflowSections, byte standardSize) {

        //находим все части рекуператора (подразумевается что он один) и передаем его на расчет
        for (SectionToConfigurator inflowRecuperator : inflowSections) {
            if (inflowRecuperator.getSectionType().getId() == ROTOR_RECUPERATOR_ID) {
                for (SectionToConfigurator outflowRecuperator : outflowSections) {
                    if (outflowRecuperator.getSectionType().getId() == ROTOR_RECUPERATOR_ID) {
                        List<SectionToConfigurator> finishCalculatedRotor = calculateRecuperator(
                                inflowConsumption,
                                outflowConsumption,
                                inflowRecuperator,
                                outflowRecuperator,
                                standardSize);

                        inflowRecuperator.setCalculated(finishCalculatedRotor.get(0).getCalculated());
                        outflowRecuperator.setCalculated(finishCalculatedRotor.get(1).getCalculated());
                        outflowRecuperator.setForm(new ArrayList<>());

                        int selectedRecuperatorId = Objects.requireNonNull(getSelectedSection(standardSize)).getId();
                        inflowRecuperator.setSelectedSectionId(selectedRecuperatorId);
                        outflowRecuperator.setSelectedSectionId(selectedRecuperatorId);
                    }
                }
            }
        }
    }

    private List<SectionToConfigurator> calculateRecuperator(int inflowConsumption, int outflowConsumption,
                                                            SectionToConfigurator inflowRecuperator, SectionToConfigurator outflowRecuperator, byte standardSize) {

        return rotorRecuperatorSelectionService.selectRecuperator(
                inflowConsumption, outflowConsumption, standardSize, inflowRecuperator, outflowRecuperator);
    }

    private Section getSelectedSection(byte standardSize) {
        List<Section> selectedSections = sectionRepository.getSelectedSectionByStandardSizeAndSectionTypeId(
                standardSize,
                ROTOR_RECUPERATOR_ID);
        if (!selectedSections.isEmpty()) {
            return selectedSections.get(0);
        } else {
            throw new SelectedHeatExchangerNotFound();
        }
    }
}
