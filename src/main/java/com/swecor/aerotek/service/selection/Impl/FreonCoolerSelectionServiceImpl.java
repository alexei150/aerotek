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

import java.util.List;

import static com.swecor.aerotek.service.Constants.FREON_COOLER_ID;

@Service
public class FreonCoolerSelectionServiceImpl implements HeatExchangerSelectionService {

    private final RoenEstHeatExchangerRepository heatExchangerRepository;

    private final VariableElementRepository variableElementRepository;

    private final RoenEstClient roenEstClient;

    public FreonCoolerSelectionServiceImpl(RoenEstHeatExchangerRepository heatExchangerRepository, VariableElementRepository variableElementRepository, RoenEstClient roenEstClient) {
        this.heatExchangerRepository = heatExchangerRepository;
        this.variableElementRepository = variableElementRepository;
        this.roenEstClient = roenEstClient;
    }

    @Override
    public SectionToConfigurator selectHeatExchanger(int flowConsumption, byte standardSize, SectionToConfigurator sectionToConfigurator) {

        //По standard_size и section type id получаем отсортированный список index теплообменника
        List<VariableElement> variableElementList = variableElementRepository.getByStandardSizeAndSectionTypeIdOrderByOrderKeyAsc(standardSize, FREON_COOLER_ID);
        if (variableElementList.isEmpty()) {
            throw new ListOfVariableElementsIsEmpty("Фреоновый охладитель");
        }

        RoenEstResponse roenEstResponse;

        //Перебераем index
        for (VariableElement variableElement : variableElementList) {

            // -> по каждому index и mode вытягиваем из бд входные параметры для dll
            RoenEstHeatExchanger roenEstHeatExchangerDB = heatExchangerRepository.findByModeAndCode((byte) 4, variableElement.getIndex()).get(0);

                // -> направляем  соедененные входные данные введенные пользователем и полученные из бд, в dll
                // -> если получаем код ответа RECALC_NO_ERROR, то вставляем параметры response dll  в посчитанную секцию теплообменника
                roenEstResponse = getHeaterDll(flowConsumption, sectionToConfigurator.getForm(), roenEstHeatExchangerDB);

                // -> если получаем код ответа RECALC_NOERROR, то вставляем параметры response dll  в посчитанную секцию теплообменника
                if (roenEstResponse.getStatus().equals("RECALC_NOERROR")) {
                    return assignCalculatedParameters(sectionToConfigurator, roenEstResponse, roenEstHeatExchangerDB.getHeaderValue2());
                } else throw new RoenEsdDllException(roenEstResponse.getStatus());
        }
        throw new RoenEsdDllException("Не удалось подобрать теплообменник");
    }

    private RoenEstResponse getHeaterDll(int flowConsumption, List<ParameterToSelection> formParameters, RoenEstHeatExchanger roenEstHeatExchangerDB) {

        RoenEstRequest request = new RoenEstRequest();
        request.setFluidSpecification(0);

        for (ParameterToSelection parameterToSelection : formParameters) {
            switch (parameterToSelection.getParam().getName()) {
                case ("Температура воздуха на входе") -> request.setAirInletTemperature(Double.parseDouble(parameterToSelection.getValue()));
                case ("Относительная влажность воздуха на входе") -> request.setAirInletHumidity(Double.parseDouble(parameterToSelection.getValue()));
                case ("Марка фреона") -> {
                    switch (parameterToSelection.getValue()) {
                        case ("R22") -> request.setFluid(4);
                        case ("R123") -> request.setFluid(5);
                        case ("R134a") -> request.setFluid(6);
                        case ("R152a") -> request.setFluid(7);
                        case ("R404A") -> request.setFluid(8);
                        case ("R407C") -> request.setFluid(9);
                        case ("R410A") -> request.setFluid(10);
                        case ("R507A") -> request.setFluid(11);
                    }
                }
                case ("Температура кипения хладогента") -> request.setTempEvaporationOrCondensation(Double.parseDouble(parameterToSelection.getValue()));
            }
        }

        RoenEstRequest builtRequest = request.toBuilder().
                mode(4).
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

        System.out.println(builtRequest);
        return roenEstClient.getHeatExchanger(builtRequest);

    }

    private SectionToConfigurator assignCalculatedParameters(SectionToConfigurator section, RoenEstResponse roenEstResponse, int headerValue2) {

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
                case ("Потери давления хладогента") -> parameter.setValue(String.format("%.2f", roenEstResponse.getFluidPressureDrop()));
                case ("Диаметры патрубков теплообменника") -> parameter.setValue(String.valueOf(headerValue2));

            }
        }
        return section;
    }
}
