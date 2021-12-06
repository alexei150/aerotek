package com.swecor.aerotek.service.selection;

import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggFansResponse;

public interface FanSelectionService {

    ZiehlAbeggFansResponse selectFan(byte standardSize, int flowConsumption, double workingPointPressure);
}
