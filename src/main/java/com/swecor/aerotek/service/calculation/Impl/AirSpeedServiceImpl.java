package com.swecor.aerotek.service.calculation.Impl;

import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.service.calculation.AirSpeedService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AirSpeedServiceImpl implements AirSpeedService {

    @Override
    public double getAirSpeed(Section section, int airConsumption) {

        return airConsumption/section.getSectionArea()/3600;
    }
}
