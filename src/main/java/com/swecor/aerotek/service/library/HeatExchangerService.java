package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.selection.integration.heaterExchanger.HeatExchangerDTO;
import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstHeatExchanger;

import java.util.List;

public interface HeatExchangerService {

    RoenEstHeatExchanger createHeatExchanger(HeatExchangerDTO heatExchangerRequestDTO);

    RoenEstHeatExchanger getHeatExchanger(int id);

    List<RoenEstHeatExchanger> showHeatExchanger();

    RoenEstHeatExchanger updateHeatExchanger(HeatExchangerDTO heatExchangerRequestDTO);

    void deleteHeatExchanger(int id);
}
