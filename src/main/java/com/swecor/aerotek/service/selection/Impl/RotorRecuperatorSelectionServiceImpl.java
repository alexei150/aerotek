package com.swecor.aerotek.service.selection.Impl;

import com.swecor.aerotek.model.library.section.VariableElement;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.integration.klingenburg.KlingenburgRequest;
import com.swecor.aerotek.model.selection.integration.klingenburg.KlingenburgResponse;
import com.swecor.aerotek.persist.library.VariableElementRepository;
import com.swecor.aerotek.rest.exceptions.selection.KlingenburgDllException;
import com.swecor.aerotek.rest.exceptions.selection.ListOfVariableElementsIsEmpty;
import com.swecor.aerotek.rest.internalApi.roenEst.KlingenburgClient;
import com.swecor.aerotek.service.selection.RecuperatorSelectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.swecor.aerotek.service.Constants.*;

@Service
@Transactional
public class RotorRecuperatorSelectionServiceImpl implements RecuperatorSelectionService {

    private final VariableElementRepository variableElementRepository;

    private final KlingenburgClient klingenburgClient;

    public RotorRecuperatorSelectionServiceImpl(VariableElementRepository variableElementRepository, KlingenburgClient klingenburgClient) {
        this.variableElementRepository = variableElementRepository;
        this.klingenburgClient = klingenburgClient;
    }

    @Override
    public List<SectionToConfigurator> selectRecuperator(int inFlowConsumption, int outFlowConsumption, byte standardSize,
                                                         SectionToConfigurator inflowRecuperator, SectionToConfigurator outflowRecuperator) {

        List<SectionToConfigurator> inflowAmdOutflowRecuperator = new ArrayList<>();

        //По standard_size и section type id получаем отсортированный список index рекуператоров
        List<VariableElement> variableElementList = variableElementRepository.getByStandardSizeAndSectionTypeIdOrderByOrderKeyAsc(standardSize, ROTOR_RECUPERATOR_ID);
        if (variableElementList.isEmpty()) {
            throw new ListOfVariableElementsIsEmpty("Роторный рекуператор");
        }

        KlingenburgResponse klingenburgResponse = new KlingenburgResponse();
        KlingenburgResponse klingenburgTransitionResponse = new KlingenburgResponse();

        //Перебераем index
        for (VariableElement variableElement : variableElementList) {

            String rotorIndex = variableElement.getIndex();

            // -> направляем  данные введенные пользователем в dll
            klingenburgResponse = getRotorRecuperator(inFlowConsumption, outFlowConsumption, inflowRecuperator.getForm(), rotorIndex);

            // -> переходный период
            klingenburgTransitionResponse = getTransitionRotorRecuperator(inFlowConsumption, outFlowConsumption, inflowRecuperator.getForm(), rotorIndex);

            // -> если получаем код ошибки 0 или 50, то вставляем параметры response dll  в посчитанные секции рекуператора
            if ((klingenburgResponse.getErrorCode() == 0 || klingenburgResponse.getErrorCode() == 50) &&
                    (klingenburgTransitionResponse.getErrorCode() == 0 || klingenburgTransitionResponse.getErrorCode() == 50)) {
                inflowAmdOutflowRecuperator.add(assignCalculatedParametersToInflow(inflowRecuperator, klingenburgResponse, klingenburgTransitionResponse, rotorIndex));
                inflowAmdOutflowRecuperator.add(assignCalculatedParametersToOutflow(outflowRecuperator, klingenburgResponse, klingenburgTransitionResponse, rotorIndex));
                return inflowAmdOutflowRecuperator;
            }
        }

        if (klingenburgResponse.getErrorCode() != 0 || klingenburgResponse.getErrorCode() != 50) {
            throw new KlingenburgDllException(klingenburgResponse.getErrorMessage());
        }
        if (klingenburgTransitionResponse.getErrorCode() != 0 || klingenburgTransitionResponse.getErrorCode() != 50) {
            throw new KlingenburgDllException(klingenburgTransitionResponse.getErrorMessage());
        }
        throw new KlingenburgDllException("Не удалось подобрать рекуператор Klingenburg");
    }

    private KlingenburgResponse getRotorRecuperator(int inflowConsumption, int outflowConsumption, List<ParameterToSelection> formParameters, String rotorIndex) {

        KlingenburgRequest request = new KlingenburgRequest();

        for (ParameterToSelection parameterToSelection : formParameters) {
            switch (parameterToSelection.getParam().getName()) {
                case ("Температура приточного воздуха на входе") -> request.setColdTempIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Относительная влажность приточного воздуха на входе") -> request.setColdHumidityIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Температура вытяжного воздуха на входе") -> request.setWarmTempIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Относительная влажность вытяжного воздуха на входе") -> request.setWarmHumidityIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Тип ротора") -> {
                    for (Map.Entry<Character, String> entry : ROTOR_TYPE_MAP.entrySet()) {
                        if (entry.getValue().equals(parameterToSelection.getValue())) {
                            request.setRotorType(entry.getKey());
                        }
                    }
                }
            }
        }

        KlingenburgRequest builtRequest = request.toBuilder().
                airPressure(1020).
                coldVolumeIn(inflowConsumption).
                warmVolumeIn(outflowConsumption).
                waveHeight(rotorIndex.substring(6, 9)).
                outerDiameter(Integer.parseInt(rotorIndex.substring(rotorIndex.lastIndexOf("-") + 1))).
                build();

        return klingenburgClient.getRotor(builtRequest);

    }

    private KlingenburgResponse getTransitionRotorRecuperator(int inflowConsumption, int outflowConsumption, List<ParameterToSelection> formParameters, String rotorIndex) {

        KlingenburgRequest transitionRequest = new KlingenburgRequest();

        for (ParameterToSelection parameterToSelection : formParameters) {
            switch (parameterToSelection.getParam().getName()) {
                case ("Температура приточного воздуха на входе ") -> transitionRequest.setColdTempIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Относительная влажность приточного воздуха на входе ") -> transitionRequest.setColdHumidityIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Температура вытяжного воздуха на входе ") -> transitionRequest.setWarmTempIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Относительная влажность вытяжного воздуха на входе ") -> transitionRequest.setWarmHumidityIn(Double.parseDouble(parameterToSelection.getValue()));
                case ("Тип ротора") -> {
                    for (Map.Entry<Character, String> entry : ROTOR_TYPE_MAP.entrySet()) {
                        if (entry.getValue().equals(parameterToSelection.getValue())) {
                            transitionRequest.setRotorType(entry.getKey());
                        }
                    }
                }
            }
        }

        KlingenburgRequest builtRequest = transitionRequest.toBuilder().
                airPressure(1020).
                coldVolumeIn(inflowConsumption).
                warmVolumeIn(outflowConsumption).
                waveHeight(rotorIndex.substring(6, 9)).
                outerDiameter(Integer.parseInt(rotorIndex.substring(rotorIndex.lastIndexOf("-") + 1))).
                build();

        return klingenburgClient.getRotor(builtRequest);

    }

    private SectionToConfigurator assignCalculatedParametersToInflow(SectionToConfigurator section, KlingenburgResponse klingenburgResponse, KlingenburgResponse klingenburgTransitionResponse, String index) {

        for (ParameterToSelection parameter : section.getCalculated()) {
            switch (parameter.getParam().getName()) {
                case ("Индекс рекуператора") -> parameter.setValue(index);
                case ("Скорость воздуха") -> parameter.setValue(klingenburgResponse.getColdAreaVelocity().toString());
                case ("Потери давления воздуха") -> parameter.setValue(klingenburgResponse.getColdPressureOut().toString());
                case ("Температура воздуха на выходе") -> parameter.setValue(klingenburgResponse.getColdTemperatureOut().toString());
                case ("Относительная влажность воздуха на выходе") -> parameter.setValue(klingenburgResponse.getColdHumidity().toString());
                case ("Тепловая мощность") -> parameter.setValue(klingenburgResponse.getColdPower().toString());
                case ("Эффективность рекуператора") -> parameter.setValue(klingenburgResponse.getEfficiency().toString());
                case ("Тип частотного регулятора") -> parameter.setValue("-");

                case ("Скорость воздуха ") -> parameter.setValue(klingenburgTransitionResponse.getColdAreaVelocity().toString());
                case ("Потери давления воздуха ") -> parameter.setValue(klingenburgTransitionResponse.getColdPressureOut().toString());
                case ("Температура воздуха на выходе ") -> parameter.setValue(klingenburgTransitionResponse.getColdTemperatureOut().toString());
                case ("Относительная влажность воздуха на выходе ") -> parameter.setValue(klingenburgTransitionResponse.getColdHumidity().toString());
            }
        }
        return section;
    }

    private SectionToConfigurator assignCalculatedParametersToOutflow(SectionToConfigurator section, KlingenburgResponse klingenburgResponse, KlingenburgResponse klingenburgTransitionResponse, String index) {

        for (ParameterToSelection parameter : section.getCalculated()) {
            switch (parameter.getParam().getName()) {
                case ("Индекс рекуператора") -> parameter.setValue(index);
                case ("Скорость воздуха") -> parameter.setValue(klingenburgResponse.getWarmAreaVelocity().toString());
                case ("Потери давления воздуха") -> parameter.setValue(klingenburgResponse.getWarmPressureOut().toString());
                case ("Температура воздуха на выходе") -> parameter.setValue(klingenburgResponse.getWarmTemperatureOut().toString());
                case ("Относительная влажность воздуха на выходе") -> parameter.setValue(klingenburgResponse.getWarmHumidity().toString());
                case ("Тепловая мощность") -> parameter.setValue(klingenburgResponse.getWarmPower().toString());
                case ("Эффективность рекуператора") -> parameter.setValue(klingenburgResponse.getEfficiency().toString());
                case ("Тип частотного регулятора") -> parameter.setValue("-");

                case ("Скорость воздуха ") -> parameter.setValue(klingenburgTransitionResponse.getWarmAreaVelocity().toString());
                case ("Потери давления воздуха ") -> parameter.setValue(klingenburgTransitionResponse.getWarmPressureOut().toString());
                case ("Температура воздуха на выходе ") -> parameter.setValue(klingenburgTransitionResponse.getWarmTemperatureOut().toString());
                case ("Относительная влажность воздуха на выходе ") -> parameter.setValue(klingenburgTransitionResponse.getWarmHumidity().toString());
            }
        }
        return section;
    }
}


