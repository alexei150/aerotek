package com.swecor.aerotek.service.calculation;

import com.swecor.aerotek.model.library.section.Section;

public interface PressureLossService {

    double getPressureLoss(Section section, int airConsumptionInput);
}
