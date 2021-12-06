package com.swecor.aerotek.service.selection.Impl;

import com.swecor.aerotek.model.library.section.VariableElement;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstHeatExchanger;
import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstRequest;
import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstResponse;
import com.swecor.aerotek.persist.library.VariableElementRepository;
import com.swecor.aerotek.persist.selction.RoenEstHeatExchangerRepository;
import com.swecor.aerotek.rest.exceptions.selection.ListOfVariableElementsIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.RoenEsdDllException;
import com.swecor.aerotek.rest.internalApi.roenEst.RoenEstClient;
import com.swecor.aerotek.service.selection.HeatExchangerSelectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.swecor.aerotek.service.Constants.*;

@Service
@Transactional
public class HeaterSelectionServiceImpl implements HeatExchangerSelectionService {

    private final RoenEstHeatExchangerRepository heatExchangerRepository;

    private final VariableElementRepository variableElementRepository;

    private final RoenEstClient roenEstClient;

    public HeaterSelectionServiceImpl(RoenEstHeatExchangerRepository heatExchangerRepository, VariableElementRepository variableElementRepository, RoenEstClient roenEstClient) {
        this.heatExchangerRepository = heatExchangerRepository;
        this.variableElementRepository = variableElementRepository;
        this.roenEstClient = roenEstClient;
    }

    @Override
    public SectionToConfigurator selectHeatExchanger(int flowConsumption, byte standardSize, SectionToConfigurator sectionToConfigurator) {

        //По standard_size и section type id получаем отсортированный список index теплообменника
        List<VariableElement> variableElementList = variableElementRepository.getByStandardSizeAndSectionTypeIdOrderByOrderKeyAsc(standardSize, HEATER_ID);
        if (variableElementList.isEmpty()) {
            throw new ListOfVariableElementsIsEmpty("Водяной нагреватель");
        }

        RoenEstResponse roenEstResponse = new RoenEstResponse();
        RoenEstResponse roenEstTransitionResponse = new RoenEstResponse();


        //Перебераем index
        for (VariableElement variableElement : variableElementList) {

            // -> по каждому index, и mode вытягиваем из бд входные параметры для dll
            RoenEstHeatExchanger roenEstHeatExchangerDB = heatExchangerRepository.findByModeAndCode((byte) 1, variableElement.getIndex()).get(0);

                // -> направляем  соедененные входные данные введенные пользователем и полученные из бд, в dll
                // -> если получаем код ответа RECALC_NO_ERROR, то вставляем параметры response dll  в посчитанную секцию теплообменника
                roenEstResponse = getHeaterDll(flowConsumption, sectionToConfigurator.getForm(), roenEstHeatExchangerDB);

                // -> переходный период
                roenEstTransitionResponse = getTransitionHeaterDll(flowConsumption, sectionToConfigurator.getForm(), roenEstHeatExchangerDB);

                // -> если получаем код ответа RECALC_NO_ERROR, то вставляем параметры response dll  в посчитанную секцию теплообменника
                if (roenEstResponse.getStatus().equals("RECALC_NOERROR") && roenEstTransitionResponse.getStatus().equals("RECALC_NOERROR")) {
                    return assignCalculatedParameters(sectionToConfigurator, roenEstResponse, roenEstTransitionResponse, roenEstHeatExchangerDB.getHeaderValue2());
                }
        }

        if (!roenEstResponse.getStatus().equals("RECALC_NOERROR")) {
            throw new RoenEsdDllException(roenEstResponse.getStatus());
        }
        if (!roenEstTransitionResponse.getStatus().equals("RECALC_NOERROR")) {
            throw new RoenEsdDllException(roenEstTransitionResponse.getStatus());
        }
        throw new RoenEsdDllException("Не удалось подобрать теплообменник");
    }

    private RoenEstResponse getHeaterDll(int flowConsumption, List<ParameterToSelection> formParameters, RoenEstHeatExchanger roenEstHeatExchangerDB) {

        RoenEstRequest request = new RoenEstRequest();

        for (ParameterToSelection parameterToSelection : formParameters) {
            switch (parameterToSelection.getParam().getName()) {
                case ("Температура воздуха на входе") -> request.setAirInletTemperature(Double.parseDouble(parameterToSelection.getValue()));
                case ("Относительная влажность воздуха на входе") -> request.setAirInletHumidity(Double.parseDouble(parameterToSelection.getValue()));
                case ("Тип теплоносителя") -> {
                    switch (parameterToSelection.getValue()) {
                        case ("Вода") -> request.setFluid(1);
                        case ("Этиленгликоль") -> request.setFluid(2);
                        case ("Пропиленгликоль") -> request.setFluid(3);
                    }
                }
                case ("Температура теплоносителя на входе") -> request.setFluidInletTemperature(Double.parseDouble(parameterToSelection.getValue()));
                case ("Температура теплоносителя на выходе") -> request.setFluidOutletTemperature(Double.parseDouble(parameterToSelection.getValue()));
            }
        }

        //если теплоноситель вода, то fluidSpecification = 0, иначе = "%-содержание теплоносителя в воде" введенные пользователем
        for (ParameterToSelection parameterToSelection : formParameters) {
            if (parameterToSelection.getParam().getName().equals("%-содержание теплоносителя в воде")) {
                if (request.getFluid() == 1) {
                    request.setFluidSpecification(0);
                } else {
                    request.setFluidSpecification(Double.parseDouble(parameterToSelection.getValue()));
                }
            }
        }

        RoenEstRequest builtRequest = request.toBuilder().
                mode(1).
                airFlowRate(flowConsumption).
                geometry(roenEstHeatExchangerDB.getGeometry()).
                length(roenEstHeatExchangerDB.getLength()).
                height(roenEstHeatExchangerDB.getHeight()).
                numRows(roenEstHeatExchangerDB.getNumRows()).
                tubesType(roenEstHeatExchangerDB.getTubesType()).
                finSpacing(roenEstHeatExchangerDB.getFinSpacing()).
                finType(roenEstHeatExchangerDB.getFinType()).
                circuitsType(roenEstHeatExchangerDB.getCircuitsType()).
                numCircuits(roenEstHeatExchangerDB.getNumCircuits()).
                headerConfiguration(roenEstHeatExchangerDB.getHeaderConfiguration()).
                headerValue1(roenEstHeatExchangerDB.getHeaderValue1()).
                headerValue2(roenEstHeatExchangerDB.getHeaderValue2()).
                build();

        return roenEstClient.getHeatExchanger(builtRequest);

    }

    private RoenEstResponse getTransitionHeaterDll(int flowConsumption, List<ParameterToSelection> formParameters, RoenEstHeatExchanger roenEstHeatExchangerDB) {

        RoenEstRequest request = new RoenEstRequest();

        for (ParameterToSelection parameterToSelection : formParameters) {
            switch (parameterToSelection.getParam().getName()) {
                case ("Температура воздуха на входе ") -> request.setAirInletTemperature(Double.parseDouble(parameterToSelection.getValue()));
                case ("Относительная влажность воздуха на входе ") -> request.setAirInletHumidity(Double.parseDouble(parameterToSelection.getValue()));
                case ("Тип теплоносителя") -> {
                    switch (parameterToSelection.getValue()) {
                        case ("Вода") -> request.setFluid(1);
                        case ("Этиленгликоль") -> request.setFluid(2);
                        case ("Пропиленгликоль") -> request.setFluid(3);
                    }
                }
                case ("Температура теплоносителя на входе ") -> request.setFluidInletTemperature(Double.parseDouble(parameterToSelection.getValue()));
                case ("Температура теплоносителя на выходе ") -> request.setFluidOutletTemperature(Double.parseDouble(parameterToSelection.getValue()));
            }
        }

        //если теплоноситель вода, то fluidSpecification = 0, иначе = "%-содержание теплоносителя в воде" введенные пользователем
        for (ParameterToSelection parameterToSelection : formParameters) {
            if (parameterToSelection.getParam().getName().equals("%-содержание теплоносителя в воде")) {
                if (request.getFluid() == 1) {
                    request.setFluidSpecification(0);
                } else {
                    request.setFluidSpecification(Double.parseDouble(parameterToSelection.getValue()));
                }
            }
        }

        RoenEstRequest builtRequest = request.toBuilder().
                mode(1).
                airFlowRate(flowConsumption).
                geometry(roenEstHeatExchangerDB.getGeometry()).
                length(roenEstHeatExchangerDB.getLength()).
                height(roenEstHeatExchangerDB.getHeight()).
                numRows(roenEstHeatExchangerDB.getNumRows()).
                tubesType(roenEstHeatExchangerDB.getTubesType()).
                finSpacing(roenEstHeatExchangerDB.getFinSpacing()).
                finType(roenEstHeatExchangerDB.getFinType()).
                circuitsType(roenEstHeatExchangerDB.getCircuitsType()).
                numCircuits(roenEstHeatExchangerDB.getNumCircuits()).
                headerConfiguration(roenEstHeatExchangerDB.getHeaderConfiguration()).
                headerValue1(roenEstHeatExchangerDB.getHeaderValue1()).
                headerValue2(roenEstHeatExchangerDB.getHeaderValue2()).
                build();

        return roenEstClient.getHeatExchanger(builtRequest);
    }

    private SectionToConfigurator assignCalculatedParameters(SectionToConfigurator section, RoenEstResponse roenEstResponse, RoenEstResponse roenEstTransitionResponse, int headerValue2) {

        for (ParameterToSelection parameter : section.getCalculated()) {
            switch (parameter.getParam().getName()) {
                case ("Индекс теплообменника") -> parameter.setValue(roenEstResponse.getCode());
                case ("Скорость воздуха") -> parameter.setValue(String.format("%.2f", roenEstResponse.getAirVelocity()));
                case ("Потери давления воздуха") -> parameter.setValue(String.format("%.2f", roenEstResponse.getAirPressureDrop()));
                case ("Температура воздуха на выходе") -> parameter.setValue(String.format("%.2f", roenEstResponse.getAirOutletTemperature()));
                case ("Относительная влажность воздуха на выходе") -> parameter.setValue(String.format("%.2f", roenEstResponse.getAirOutletHumidity()));
                case ("Температура теплоносителя на выходе") -> parameter.setValue(String.format("%.2f", roenEstResponse.getFluidOutletTemperature()));
                case ("Тепловая мощность") -> parameter.setValue(String.format("%.2f", roenEstResponse.getNominalCapacity()));
                case ("Расход теплоносителя") -> parameter.setValue(String.format("%.2f", roenEstResponse.getFluidFlowRate()));
                case ("Скорость теплоносителя в теплообменнике") -> parameter.setValue(String.format("%.2f", roenEstResponse.getFluidVelocity()));
                case ("Потери давления теплоносителя") -> parameter.setValue(String.format("%.2f", roenEstResponse.getFluidPressureDrop()));
                case ("Диаметры патрубков теплообменника") -> parameter.setValue(String.valueOf(headerValue2));

                case ("Температура воздуха на выходе ") -> parameter.setValue(String.format("%.2f", roenEstTransitionResponse.getAirOutletTemperature()));
                case ("Относительная влажность воздуха на выходе ") -> parameter.setValue(String.format("%.2f", roenEstTransitionResponse.getAirOutletHumidity()));
                case ("Тепловая мощность ") -> parameter.setValue(String.format("%.2f", roenEstTransitionResponse.getNominalCapacity()));
                case ("Расход теплоносителя ") -> parameter.setValue(String.format("%.2f", roenEstTransitionResponse.getFluidFlowRate()));
                case ("Скорость теплоносителя в теплообменнике ") -> parameter.setValue(String.format("%.2f", roenEstTransitionResponse.getFluidVelocity()));
                case ("Потери давления теплоносителя ") -> parameter.setValue(String.format("%.2f", roenEstTransitionResponse.getFluidPressureDrop()));
            }
        }
        return section;
    }
}


