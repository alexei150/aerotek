package com.swecor.aerotek.service.calculation;

import com.swecor.aerotek.model.library.section.Section;

public interface AirSpeedService {

    double getAirSpeed(Section section, int AirConsumption);
}
