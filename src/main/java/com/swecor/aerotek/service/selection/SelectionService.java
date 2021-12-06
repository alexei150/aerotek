package com.swecor.aerotek.service.selection;

import com.swecor.aerotek.model.selection.Instalation.AirCharacteristicDTO;
import com.swecor.aerotek.model.selection.Instalation.builder.InstallationBuilderDTO;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;

public interface SelectionService {

    InstallationToSelection saveInstallationBuilder(InstallationBuilderDTO installationBuilderDTO);

    InstallationToSelection updateInstallationBuilder(int id, InstallationBuilderDTO installationBuilderDTO);

    InstallationToSelection updateAirCharacteristic(int id, AirCharacteristicDTO airCharacteristicDTO);

    InstallationToSelection calculateInstallation(int id, InstallationToSelection inflowAndOutflowDTO);

    InstallationToSelection saveInstallation(int id, InstallationToSelection installation);

}
