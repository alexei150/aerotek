package com.swecor.aerotek.service.calculation.Impl;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.drawModel.DrawModel;
import com.swecor.aerotek.model.selection.Instalation.drawModel.WeightAndSize;
import com.swecor.aerotek.persist.library.ParameterRepository;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.library.SectionIsAbsent;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.service.calculation.Calculated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DrawModelServiceImpl implements Calculated {

    private final ParameterRepository parameterRepository;

    private final SectionRepository sectionRepository;

    public DrawModelServiceImpl(ParameterRepository parameterRepository, SectionRepository sectionRepository) {
        this.parameterRepository = parameterRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public InstallationToSelection calculate(InstallationToSelection installation) {

        installation.setDrawModel(calculateDrawModel(installation));

        return installation;
    }

    @Override
    public int getMySectionTypeId() {
        return 20;
    }

    private DrawModel calculateDrawModel(InstallationToSelection installation) {

        List<WeightAndSize> weightsAndSizes = getWeightAndSize(installation);

        return DrawModel.builder().
                weightsAndSizes(weightsAndSizes).
                totalWeight(getTotalWeight(weightsAndSizes)).build();
    }

    private List<WeightAndSize> getWeightAndSize(InstallationToSelection installation) {
        boolean isInflow = !installation.getInflowParameters().isEmpty();
        boolean isOutflow = !installation.getOutflowParameters().isEmpty();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {
            return Stream.concat(
                    getFlowWeightAndSize(installation.getInflowParameters(), installation.getBuilder().isOutflowIsUp(), true).stream(),
                    getFlowWeightAndSize(installation.getOutflowParameters(), installation.getBuilder().isOutflowIsUp(), false).stream()).
                    collect(Collectors.toList());
        } else if (isInflow) {
            return getFlowWeightAndSize(installation.getInflowParameters(), installation.getBuilder().isOutflowIsUp(), true);
        } else {
            return getFlowWeightAndSize(installation.getOutflowParameters(), installation.getBuilder().isOutflowIsUp(), false);
        }

    }

    private List<WeightAndSize> getFlowWeightAndSize(List<SectionToConfigurator> sectionToConfiguratorList, boolean outflowIsUp, boolean itIsInflow) {
        List<WeightAndSize> weightAndSizeList = new ArrayList<>();

        for (SectionToConfigurator sectionToConfigurator : sectionToConfiguratorList) {
            int sectionId = sectionToConfigurator.getSelectedSectionId();

            weightAndSizeList.add(
                    WeightAndSize.builder().
                            name(getConcatName(sectionToConfigurator, itIsInflow)).
                            x((int) parameterRepository.getParameter(sectionId, "Длина")).
                            y((int) parameterRepository.getParameter(sectionId, "Ширина")).
                            z((int) parameterRepository.getParameter(sectionId, "Высота")).
                            weight(parameterRepository.getParameter(sectionId, "Вес")).
                            position(sectionToConfigurator.getPosition()).
                            basis(getBasis(sectionId, outflowIsUp, itIsInflow)).
                            build());

        }
        Collections.sort(weightAndSizeList);

        return weightAndSizeList;
    }

    private String getConcatName(SectionToConfigurator sectionToConfigurator, boolean isIsInflow) {
        StringBuilder sb = new StringBuilder();
        Section section = sectionRepository.findById(sectionToConfigurator.getSelectedSectionId()).orElseThrow(SectionIsAbsent::new);

        if (isIsInflow) {
            sb.append("Приток, модуль ");
            sb.append(sectionToConfigurator.getPosition());
            sb.append(" ");
            sb.append(section.getName());
        } else {
            sb.append("Вытяжка, модуль ");
            sb.append(sectionToConfigurator.getPosition());
            sb.append(" ");
            sb.append(section.getName());
        }


        return sb.toString();
    }

    private String getBasis(int sectionId, boolean outflowIsUp, boolean isInflow) {
        if (outflowIsUp && isInflow) {
            return String.valueOf(parameterRepository.getParameter(sectionId, "Длина"));
        } else if (outflowIsUp) {
            return "-";
        } else if (isInflow) {
            return "-";
        } else {
            return String.valueOf(parameterRepository.getParameter(sectionId, "Длина"));
        }
    }

    private double getTotalWeight(List<WeightAndSize> weightsAndSizes) {
        double totalWeight = 0;

        for (WeightAndSize weightAndSize : weightsAndSizes) {
            totalWeight += weightAndSize.getWeight();
        }

        return totalWeight;
    }
}
