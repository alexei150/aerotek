package com.swecor.aerotek.service.calculation.sectionCalc;

import com.swecor.aerotek.model.library.section.ElectricHeater;
import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.LogicalException;
import com.swecor.aerotek.rest.exceptions.calculation.ParameterByNameNotFound;
import com.swecor.aerotek.rest.exceptions.library.SectionTypeIsAbsent;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.rest.exceptions.selection.ParameterValueIsNull;
import com.swecor.aerotek.service.calculation.AirSpeedService;
import com.swecor.aerotek.service.calculation.Calculated;
import com.swecor.aerotek.service.calculation.PressureLossService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.swecor.aerotek.service.Constants.*;

@Service
@Transactional
public class ElectricHeaterCalculationService implements Calculated {

    private final SectionRepository sectionRepository;
    private final PressureLossService pressureLossService;
    private final AirSpeedService airSpeedService;
    double p;

    public ElectricHeaterCalculationService(SectionRepository sectionRepository, PressureLossService pressureLossService, AirSpeedService airSpeedService) {
        this.sectionRepository = sectionRepository;
        this.pressureLossService = pressureLossService;
        this.airSpeedService = airSpeedService;
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
        return ELECTRIC_HEATER_ID;
    }

    //По формуле:
    //Q = c·V·ρ·ΔT/3600, где
    //· с— теплоёмкость воздуха, с=1,005 кДж/(кг·°С);
    //· V— расход воздуха в м3/час;
    //· Ρ— плотность воздуха, принимается среднее значение 1,2кг/м3; берем по DENSITY_MAP ближайшее большее при средней температуре Т=(T1+T2)/2
    //· ΔT— разность температур, на которую нужно нагреть воздух. = T2-T1 где T1 - температура на входе в нагреватель, Т2 - желаемая температура воздуха(или итогновая при обратном расчете)
    //Получив расчетную тепловую мощность Q, принимаем ближайший больший по мощности нагреватель для программы
    // Считаем все в обратную сторону где Q нам тепер известна, а Т2 температура на выходе
    private void calculateFlowParameters(List<SectionToConfigurator> flowParameters, int flowConsumption, byte standardSize) {

        for (SectionToConfigurator sectionToConfigurator : flowParameters) {
            if (sectionToConfigurator.getSectionType().getId() == ELECTRIC_HEATER_ID) {

                int inletTemperature = Integer.parseInt(getFormParameterValueByName(sectionToConfigurator.getForm(), INLET_TEMP).getValue());
                int inletTemperatureTransient = Integer.parseInt(getFormParameterValueByName(sectionToConfigurator.getForm(), INLET_TEMP_TRANSIENT).getValue());

                int requiredHeaterPower = calculateRequiredHeaterPower(
                        flowConsumption,
                        inletTemperature,
                        Integer.parseInt(getFormParameterValueByName(sectionToConfigurator.getForm(), REQUIRED_TEMP_OUT).getValue()));

                int requiredHeaterPowerTransient = calculateRequiredHeaterPower(
                        flowConsumption,
                        inletTemperatureTransient,
                        Integer.parseInt(getFormParameterValueByName(sectionToConfigurator.getForm(), REQUIRED_TEMP_OUT_TRANSIENT).getValue()));



                ElectricHeater requiredHeater = sectionRepository.getElectricHeater(
                        standardSize,
                        getMySectionTypeId(),
                        NUMBER_OF_CONTROL_STEPS,
                        getFormParameterValueByName(sectionToConfigurator.getForm(), NUMBER_OF_CONTROL_STEPS).getValue(),
                        HEAT_POWER,
                        requiredHeaterPower);

                ElectricHeater requiredHeaterTransient = sectionRepository.getElectricHeater(
                        standardSize,
                        getMySectionTypeId(),
                        NUMBER_OF_CONTROL_STEPS,
                        getFormParameterValueByName(sectionToConfigurator.getForm(), NUMBER_OF_CONTROL_STEPS).getValue(),
                        HEAT_POWER,
                        requiredHeaterPowerTransient);

                if (!requiredHeater.getId().equals(requiredHeaterTransient.getId())){
                    throw new LogicalException("Электрические нагреватели для основного и переходного периода не сообветствуют друг другу");
                }

                Section selectedSection = sectionRepository.findById(requiredHeater.getId()).orElseThrow(SectionTypeIsAbsent::new);

                if (selectedSection != null) {
                    sectionToConfigurator.setSelectedSectionId(selectedSection.getId());
                } else throw new LogicalException("не найден указанный электрический нагреватель");

                sectionToConfigurator.setCalculated(
                        setCalculatedParameters(
                                sectionToConfigurator.getCalculated(),
                                selectedSection,
                                flowConsumption,
                                calculateFinalHeaterTemperature(requiredHeater.getValue(), inletTemperature, flowConsumption),
                                calculateFinalHeaterTemperature(requiredHeater.getValue(), inletTemperatureTransient, flowConsumption),
                                requiredHeaterPower,
                                requiredHeaterPowerTransient,
                                requiredHeater.getValue()
                                ));
            }
        }
    }

    private int calculateRequiredHeaterPower(int airConsumption, int inletTemp, int requiredTemp) {
        setAirDensity(inletTemp, requiredTemp);
        return (int) Math.round(1.005 * airConsumption * p * (requiredTemp - inletTemp) / 3600);
    }

    //· Ρ— плотность воздуха, принимается среднее значение 1,2кг/м3; берем по DENSITY_MAP ближайшее большее при средней температуре Т=(T1+T2)/2
    private void setAirDensity(int inletTemp, int outletTemp){
        int deltaT = Math.round((inletTemp + outletTemp) / 2);

        int distance = Math.abs(DENSITY_ARRAY[0] - deltaT);
        int idx = 0;
        for (int c = 1; c < DENSITY_ARRAY.length; c++) {
            int cdistance = Math.abs(DENSITY_ARRAY[c] - deltaT);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
            }
        }
       p = DENSITY_MAP.get(DENSITY_ARRAY[idx]);
    }

    private int calculateFinalHeaterTemperature(int heaterPower, int inletTemp, int airConsumption){
        return (int) Math.round((3600*heaterPower)/(1.005*airConsumption*p) + inletTemp);
    }



    private ParameterToSelection getFormParameterValueByName(List<ParameterToSelection> formParameters, String paramName) {
        for (ParameterToSelection parameter : formParameters) {
            if (parameter.getParam().getName().equals(paramName)) {
                if (parameter.getValue() == null) {
                    throw new ParameterValueIsNull("Параметр '" + parameter.getParam().getName() + "' у КЭлектрического нагревателя == null");
                }
                return parameter;
            }
        }
        throw new ParameterByNameNotFound(paramName + "' у Электрического нагревателя");
    }

    private List<ParameterToSelection> setCalculatedParameters(
            List<ParameterToSelection> calculatedParameters, Section selectedSection, int airConsumptionInput,
            int outletTemperature, int outletTemperatureTransient, int calculatedHeatPower, int calculatedHeatPowerTransient, int heatPower) {
        for (ParameterToSelection parameter : calculatedParameters) {
            switch (parameter.getParam().getName()) {
                case (AIR_SPEED) -> parameter.setValue(String.format("%.2f", (airSpeedService.getAirSpeed(selectedSection, airConsumptionInput))));
                case (PRESSURE_LOSS) -> parameter.setValue(String.format("%.2f", (pressureLossService.getPressureLoss(selectedSection, airConsumptionInput))));
                case (OUTLET_TEMP) -> parameter.setValue(String.valueOf(outletTemperature));
                case (RELATIVE_HUMIDITY) -> parameter.setValue("-");
                case (CALCULATED_HEAT_POWER) -> parameter.setValue(String.valueOf(calculatedHeatPower));
                case (HEAT_POWER) -> parameter.setValue(String.valueOf(heatPower));
                case (OUTLET_TEMP_TRANSIENT) -> parameter.setValue(String.valueOf(outletTemperatureTransient));
                case (RELATIVE_HUMIDITY_TRANSIENT) -> parameter.setValue("-");
                case (CALCULATED_HEAT_POWER_TRANSIENT) -> parameter.setValue(String.valueOf(calculatedHeatPowerTransient));
            }
        }
        return calculatedParameters;
    }
}
