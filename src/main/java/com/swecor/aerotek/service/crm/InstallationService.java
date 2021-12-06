package com.swecor.aerotek.service.crm;

import com.swecor.aerotek.model.crm.InstallationDTO;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;

import java.io.IOException;
import java.util.List;

public interface InstallationService {

    List<InstallationDTO> showAllInstallations();

    List<InstallationDTO> showInstallations(String email);

    List<InstallationDTO> deleteInstallation(String email, int id);

    List<InstallationDTO> deleteInstallationAdmin(int id);

    List<InstallationDTO> copyInstallation(String email, int id) throws IOException, ClassNotFoundException;

    InstallationToSelection editInstallation(int id);
}