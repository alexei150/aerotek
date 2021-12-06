package com.swecor.aerotek.service.calculation;

import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;

public interface Calculated {

    InstallationToSelection calculate(InstallationToSelection installation);

    int getMySectionTypeId();
}
