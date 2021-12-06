package com.swecor.aerotek.service.selection.Impl;

import com.swecor.aerotek.model.library.section.VariableElement;
import com.swecor.aerotek.model.library.sectionType.SectionType;
import com.swecor.aerotek.model.selection.Instalation.*;
import com.swecor.aerotek.model.selection.Instalation.flow.DefaultParameter;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.DefaultParameterDictionary;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.Instalation.builder.InstallationBuilderDTO;
import com.swecor.aerotek.persist.library.SectionTypeRepository;
import com.swecor.aerotek.persist.library.VariableElementRepository;
import com.swecor.aerotek.persist.selction.InstallationToSelectionRepository;
import com.swecor.aerotek.rest.exceptions.library.InstallationToSelectionIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.SectionTypeIsAbsent;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.InstallationIsNotCalculated;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.calculation.Impl.AcousticPerformanceServiceImpl;
import com.swecor.aerotek.service.calculation.Impl.DrawModelServiceImpl;
import com.swecor.aerotek.service.calculation.sectionCalc.*;
import com.swecor.aerotek.service.selection.SelectionService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import static com.swecor.aerotek.service.Constants.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SelectionServiceImpl implements SelectionService {

    private final InstallationToSelectionRepository installationToSelectionRepository;

    private final SectionTypeRepository sectionTypeRepository;

    private final VariableElementRepository variableElementRepository;

    private final Map<Integer, Calculated> calculatedMap;

    private byte position;

    public SelectionServiceImpl(InstallationToSelectionRepository installationToSelectionRepository,
                                SectionTypeRepository sectionTypeRepository,
                                AirValveCalculationService airValveCalculationService,
                                EmptySectionCalculationService emptySectionCalculationService,
                                MufflerCalculationService mufflerCalculationService,
                                FanFreeWheelCalculationService freeWheelCalculationService,
                                PocketFilterCalculationService pocketFilterCalculationService,
                                CassetteFilterCalculationService cassetteFilterCalculationService,
                                HeaterCalculationService heaterCalculationService,
                                CoolerCalculationService coolerCalculationService,
                                FreonCoolerCalculationService freonCoolerCalculationService,
                                RotorRecuperatorCalculationService rotorRecuperatorCalculationService,
                                ElectricHeaterCalculationService electricHeaterCalculationService,

                                DrawModelServiceImpl weightAndSizeService,
                                AcousticPerformanceServiceImpl acousticPerformanceService, VariableElementRepository variableElementRepository, EntityManager entityManager) {
        this.installationToSelectionRepository = installationToSelectionRepository;
        this.sectionTypeRepository = sectionTypeRepository;
        this.variableElementRepository = variableElementRepository;

        Map<Integer, Calculated> calculatedMap = new HashMap<>();
        calculatedMap.put(freeWheelCalculationService.getMySectionTypeId(), freeWheelCalculationService);
        calculatedMap.put(mufflerCalculationService.getMySectionTypeId(), mufflerCalculationService);
        calculatedMap.put(airValveCalculationService.getMySectionTypeId(), airValveCalculationService);
        calculatedMap.put(emptySectionCalculationService.getMySectionTypeId(), emptySectionCalculationService);
        calculatedMap.put(pocketFilterCalculationService.getMySectionTypeId(), pocketFilterCalculationService);
        calculatedMap.put(cassetteFilterCalculationService.getMySectionTypeId(), cassetteFilterCalculationService);
        calculatedMap.put(heaterCalculationService.getMySectionTypeId(), heaterCalculationService);
        calculatedMap.put(coolerCalculationService.getMySectionTypeId(), coolerCalculationService);
        calculatedMap.put(freonCoolerCalculationService.getMySectionTypeId(), freonCoolerCalculationService);
        calculatedMap.put(rotorRecuperatorCalculationService.getMySectionTypeId(), rotorRecuperatorCalculationService);
        calculatedMap.put(electricHeaterCalculationService.getMySectionTypeId(), electricHeaterCalculationService);

        calculatedMap.put(weightAndSizeService.getMySectionTypeId(), weightAndSizeService);
        calculatedMap.put(acousticPerformanceService.getMySectionTypeId(), acousticPerformanceService);
        this.calculatedMap = calculatedMap;
    }

    @Override
    public InstallationToSelection saveInstallationBuilder(InstallationBuilderDTO installationBuilderDTO) {
        return installationToSelectionRepository.save(InstallationToSelection.builder().
                builder(installationBuilderDTO).
                status(InstallationSelectionStatus.BUILDER).
                build());
    }

    @Override
    public InstallationToSelection updateInstallationBuilder(int id, InstallationBuilderDTO installationBuilderDTO) {
        InstallationToSelection installationToSelectionDB = installationToSelectionRepository.findById(id).orElseThrow(InstallationToSelectionIsAbsent::new);

        if (installationToSelectionDB.getStatus() != InstallationSelectionStatus.AIR_APPLIED) {
            InstallationToSelection updatedInstallation = installationToSelectionDB.toBuilder().
                    builder(installationBuilderDTO).
                    build();

            return installationToSelectionRepository.save(updatedInstallation);
        } else {
            InstallationToSelection updatedInstallationWithParams = installationToSelectionDB.toBuilder().
                    builder(installationBuilderDTO).
                    inflowParameters(getSectionsToConfigurator(installationBuilderDTO.getInflow().getSectionTypes(), installationToSelectionDB.getStandardSize())).
                    outflowParameters(getSectionsToConfigurator(installationBuilderDTO.getOutflow().getSectionTypes(), installationToSelectionDB.getStandardSize())).
                    build();

            return installationToSelectionRepository.save(updatedInstallationWithParams);
        }
    }

    @Override
    public InstallationToSelection updateAirCharacteristic(int id, AirCharacteristicDTO airCharacteristicDTO) {

        InstallationToSelection installationToSelectionDB = installationToSelectionRepository.findById(id).orElseThrow(InstallationToSelectionIsAbsent::new);

        position = 0;

        InstallationToSelection updatedInstallation = installationToSelectionDB.toBuilder().
                name(airCharacteristicDTO.getName()).
                address(airCharacteristicDTO.getAddress()).
                inflowConsumption(airCharacteristicDTO.getInflowConsumption()).
                inflowPressure(airCharacteristicDTO.getInflowPressure()).
                outflowConsumption(airCharacteristicDTO.getOutflowConsumption()).
                outflowPressure(airCharacteristicDTO.getOutflowPressure()).
                inflowTemperature(airCharacteristicDTO.getInflowTemperature()).
                outflowTemperature(airCharacteristicDTO.getOutflowTemperature()).
                standardSize(airCharacteristicDTO.getStandardSize()).
                description(airCharacteristicDTO.getDescription()).
                thickness(airCharacteristicDTO.getThickness()).
                outside(airCharacteristicDTO.isOutside()).
                summerMode(airCharacteristicDTO.isSummerMode()).
                winterMode(airCharacteristicDTO.isWinterMode()).
                inflowParameters(getSectionsToConfigurator(installationToSelectionDB.getBuilder().getInflow().getSectionTypes(), airCharacteristicDTO.getStandardSize())).
                outflowParameters(getSectionsToConfigurator(installationToSelectionDB.getBuilder().getOutflow().getSectionTypes(), airCharacteristicDTO.getStandardSize())).
                status(InstallationSelectionStatus.AIR_APPLIED).
                build();

        setTemperatureParameters(updatedInstallation);

        return installationToSelectionRepository.save(updatedInstallation);
    }

    @Override
    public InstallationToSelection calculateInstallation(int id, InstallationToSelection installationRequest) {

        InstallationToSelection installationDB = installationToSelectionRepository.findById(id).orElseThrow(InstallationToSelectionIsAbsent::new);

        InstallationToSelection updatedInstallation = installationDB.toBuilder().
                inflowParameters(installationRequest.getInflowParameters()).
                outflowParameters(installationRequest.getOutflowParameters()).
                thickness(installationRequest.getThickness()).
                insertion(installationRequest.isInsertion()).
                build();

        List<Integer> sectionTypes = getSortedSectionTypesToCalculate(installationDB);

        for (int sectionType : sectionTypes) {
            calculatedMap.get(sectionType).calculate(updatedInstallation);
        }

        calculatedMap.get(20).calculate(updatedInstallation);
        calculatedMap.get(21).calculate(updatedInstallation);

        updatedInstallation.setStatus(InstallationSelectionStatus.CALCULATED);

        return installationToSelectionRepository.save(updatedInstallation);

    }

    @Override
    public InstallationToSelection saveInstallation(int id, InstallationToSelection installation) {
        if (installation.getStatus().equals(InstallationSelectionStatus.CALCULATED)) {
            InstallationToSelection installationDB = installationToSelectionRepository.findById(id).orElseThrow(InstallationToSelectionIsAbsent::new);
            installationDB.setStatus(InstallationSelectionStatus.DONE);
            return installationToSelectionRepository.save(installationDB);
        } else {
            throw new InstallationIsNotCalculated();
        }
    }

    private List<SectionToConfigurator> getSectionsToConfigurator(List<Integer> sectionTypesId, byte standardSize) {
        if (sectionTypesId == null) {
            return null;
        }

        List<SectionToConfigurator> sectionToConfiguratorList = new ArrayList<>();

        for (int id : sectionTypesId) {

            SectionType findSection = sectionTypeRepository.findById(id).orElseThrow(SectionTypeIsAbsent::new);

            sectionToConfiguratorList.add(SectionToConfigurator.builder().
                    calculated(getParametersToSelection(findSection, true, standardSize)).
                    form(getParametersToSelection(findSection, false, standardSize)).
                    sectionType(findSection).
                    position(++position).
                    build());
        }
        return sectionToConfiguratorList;
    }

    private List<ParameterToSelection> getParametersToSelection(SectionType sectionType, boolean isCalculated, byte standardSize) {
        List<ParameterToSelection> parameterToSelectionList = new ArrayList<>();

        for (DefaultParameter defaultParameter : sectionType.getDefaultParameters()) {
            if (defaultParameter.isCalculating() == isCalculated) {
                parameterToSelectionList.add(ParameterToSelection.builder().
                        param(defaultParameter).
                        build());
            }
        }

        if (sectionType.getId() == ROTOR_RECUPERATOR_ID && !isCalculated) {
            setRotorVariableParameters(sectionType, standardSize, parameterToSelectionList);
        }

        Collections.sort(parameterToSelectionList);
        return parameterToSelectionList;
    }

    private void setTemperatureParameters(InstallationToSelection installation) {

        //перебрать все секции, получить теплообменники
        //цикл
        //получить конкретную секцию
        //перебрать ее вводимые параметры и заполнить данными

        if (installation.getInflowParameters() != null) {
            for (SectionToConfigurator sectionToConfigurator : installation.getInflowParameters()) {
                Integer sectionTypeId = sectionToConfigurator.getSectionType().getId();
                if (sectionTypeId == HEATER_ID || sectionTypeId == COOLER_ID || sectionTypeId == FREON_COOLER_ID) {
                    for (ParameterToSelection parameter : sectionToConfigurator.getForm()) {
                        switch (parameter.getParam().getName()) {
                            case ("Температура воздуха на входе") -> parameter.setValue(
                                    String.valueOf(installation.getInflowTemperature().getWinterModeTemperature().getInletTemperature())
                            );
                            case ("Желаемая температура воздуха на выходе") -> parameter.setValue(
                                    String.valueOf(installation.getInflowTemperature().getWinterModeTemperature().getOutletTemperature())
                            );
                            case ("Относительная влажность воздуха на входе") -> parameter.setValue(
                                    String.valueOf(installation.getInflowTemperature().getWinterModeTemperature().getInletHumidity())
                            );
                            case ("Температура воздуха на входе ") -> parameter.setValue(
                                    String.valueOf(installation.getInflowTemperature().getSummerModeTemperature().getInletTemperature())
                            );
                            case ("Относительная влажность воздуха на входе ") -> parameter.setValue(
                                    String.valueOf(installation.getInflowTemperature().getSummerModeTemperature().getInletHumidity())
                            );
                        }
                    }
                }
            }
        }

        if (installation.getOutflowParameters() != null) {
            for (SectionToConfigurator sectionToConfigurator : installation.getOutflowParameters()) {
                Integer sectionTypeId = sectionToConfigurator.getSectionType().getId();
                if (sectionTypeId == HEATER_ID || sectionTypeId == COOLER_ID || sectionTypeId == FREON_COOLER_ID) {
                    for (ParameterToSelection parameter : sectionToConfigurator.getForm()) {
                        switch (parameter.getParam().getName()) {
                            case ("Температура воздуха на входе") -> parameter.setValue(
                                    String.valueOf(installation.getOutflowTemperature().getWinterModeTemperature().getInletTemperature())
                            );
                            case ("Относительная влажность воздуха на входе") -> parameter.setValue(
                                    String.valueOf(installation.getOutflowTemperature().getWinterModeTemperature().getInletHumidity())
                            );
                            case ("Температура воздуха на входе ") -> parameter.setValue(
                                    String.valueOf(installation.getOutflowTemperature().getSummerModeTemperature().getInletTemperature())
                            );
                            case ("Относительная влажность воздуха на входе ") -> parameter.setValue(
                                    String.valueOf(installation.getOutflowTemperature().getSummerModeTemperature().getInletHumidity())
                            );
                        }
                    }
                }
            }
        }
    }

    //т.к. Вентилятор считается последним, возвращаем отсортированный лист, где вентилятор в конце списка
    private List<Integer> getSortedSectionTypesToCalculate(InstallationToSelection installation) {

        boolean isInflow = !installation.getInflowParameters().isEmpty();
        boolean isOutflow = !installation.getOutflowParameters().isEmpty();

        List<Integer> concatList = new ArrayList<>();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {

            List<Integer> inflow = installation.getBuilder().getInflow().getSectionTypes();
            List<Integer> outflow = installation.getBuilder().getOutflow().getSectionTypes();

            concatList = Stream.concat(inflow.stream(), outflow.stream()).distinct().collect(Collectors.toList());

        } else if (isInflow) {

            List<Integer> inflow = installation.getBuilder().getInflow().getSectionTypes();

            concatList = inflow.stream().distinct().collect(Collectors.toList());

        } else if (isOutflow) {

            List<Integer> outflow = installation.getBuilder().getOutflow().getSectionTypes();

            concatList = outflow.stream().distinct().collect(Collectors.toList());
        }


        if (concatList.contains(FAN_FREE_WHEEL_ID) && concatList.indexOf(FAN_FREE_WHEEL_ID) != concatList.size() - 1) {
            Collections.swap(concatList, concatList.indexOf(FAN_FREE_WHEEL_ID), concatList.size() - 1);
        }
        return concatList;
    }

    private void setRotorVariableParameters(SectionType sectionType, byte standardSize, List<ParameterToSelection> parameterToSelectionList) {

        List<VariableElement> variableElements = variableElementRepository.getByStandardSizeAndSectionTypeIdOrderByOrderKeyAsc(standardSize, sectionType.getId());

        //вставляем в выпадающий список Тип ротора и Профиль матрицы в зависимости от того что бибилиотекарь внес в базу
        Set<String> rotorTypeDictionary = new HashSet<>();
        Set<String> matrixProfileDictionary = new HashSet<>();

        variableElements.forEach(variableElement -> {
            char rotorTypeCode = variableElement.getIndex().charAt(variableElement.getIndex().indexOf("-") + 1);
            rotorTypeDictionary.add(ROTOR_TYPE_MAP.get(rotorTypeCode));
            matrixProfileDictionary.add(variableElement.getIndex().substring(6, 9));
        });

        List<DefaultParameterDictionary> rotorTypeDefaultParameterDictionary = new ArrayList<>();
        List<DefaultParameterDictionary> matrixTypeDefaultParameterDictionary = new ArrayList<>();

        rotorTypeDictionary.forEach(rotorType -> rotorTypeDefaultParameterDictionary.add(DefaultParameterDictionary.builder().value(rotorType).build()));
        matrixProfileDictionary.forEach(matrixType -> matrixTypeDefaultParameterDictionary.add(DefaultParameterDictionary.builder().value(matrixType).build()));

        parameterToSelectionList.stream()
                .filter(p -> p.getParam().getName().equals("Тип ротора"))
                .findFirst()
                .map(p -> {
                    p.getParam().setDictionary(rotorTypeDefaultParameterDictionary);
                    return p;
                });

        parameterToSelectionList.stream()
                .filter(m -> m.getParam().getName().equals("Профиль матрицы"))
                .findFirst()
                .map(m -> {
                    m.getParam().setDictionary(matrixTypeDefaultParameterDictionary);
                    return m;
                });
    }

}
