package com.swecor.aerotek.service.calculation.Impl;

import com.swecor.aerotek.model.library.section.AirConsumption;
import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.persist.library.AirConsumptionRepository;
import com.swecor.aerotek.service.calculation.PressureLossService;
import org.springframework.stereotype.Service;

@Service
public class PressureLossServiceImpl implements PressureLossService {

    private final AirConsumptionRepository airConsumptionRepository;

    public PressureLossServiceImpl(AirConsumptionRepository airConsumptionRepository) {
        this.airConsumptionRepository = airConsumptionRepository;
    }

    @Override
    public double getPressureLoss(Section section, int airConsumptionInput) {

        //todo 3.1.2. Для Типа секции «Вентилятор радиальный (с4.1)», «Вентилятор свободное колесо (с4.2)»,
        // «Рама (с6.1)», «Гибкая вставка (с6.2)», «Ножки (с6.3)» расчет не производится, дописать в условие когда появятся эти типы секций
        if (section.getId() == 1) {
            return 0;
        }

        AirConsumption airConsumption = airConsumptionRepository.findBySectionAndConsumption(section, airConsumptionInput);

        //если есть вхождение, то возвращаем, иначе считаем по формуле ΔP=a+(L-A)/(B-A)*(b-a) п. 3.1 ТЗ
        if (airConsumption != null){
            return airConsumption.getPressure();
        } else {
            return calculatePressureLoss(section, airConsumptionInput);
        }
    }

    private double calculatePressureLoss(Section section, int airConsumptionInput){
        AirConsumption airConsumptionSmaller = airConsumptionRepository.getNearestSmaller(section.getId(), airConsumptionInput);
        AirConsumption airConsumptionGreater = airConsumptionRepository.getNearestGreater(section.getId(), airConsumptionInput);

        double L = airConsumptionInput;
        double A = airConsumptionSmaller.getConsumption();
        double a = airConsumptionSmaller.getPressure();
        double B = airConsumptionGreater.getConsumption();
        double b = airConsumptionGreater.getPressure();

        return a+(L-A)/(B-A)*(b-a);
    }

}
