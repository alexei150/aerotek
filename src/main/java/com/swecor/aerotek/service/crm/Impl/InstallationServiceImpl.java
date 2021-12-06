package com.swecor.aerotek.service.crm.Impl;

import com.swecor.aerotek.model.crm.InstallationDTO;
import com.swecor.aerotek.model.selection.Instalation.InstallationSelectionStatus;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.persist.selction.InstallationToSelectionRepository;
import com.swecor.aerotek.rest.exceptions.LogicalException;
import com.swecor.aerotek.service.crm.InstallationService;
import com.swecor.aerotek.service.selection.SelectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class InstallationServiceImpl implements InstallationService {

    @PersistenceContext
    private EntityManager entityManager;

    private final InstallationToSelectionRepository installationRepository;
    private final SelectionService selectionService;

    public InstallationServiceImpl(InstallationToSelectionRepository installationRepository, SelectionService selectionService) {
        this.installationRepository = installationRepository;
        this.selectionService = selectionService;
    }

    @Override
    public List<InstallationDTO> showAllInstallations() {
        List<InstallationDTO> installationDTOs = buildInstallations(installationRepository.findAll());
        Collections.sort(installationDTOs);
        return installationDTOs;
    }

    @Override
    public List<InstallationDTO> showInstallations(String email) {
        List<InstallationDTO> installationDTOs = buildInstallations(installationRepository.getByCreatedByAndStatusNot(email, InstallationSelectionStatus.ARCHIVE));

        Collections.sort(installationDTOs);
        return installationDTOs;
    }

    @Override
    public List<InstallationDTO> deleteInstallation(String email, int id) {
        InstallationToSelection installation = installationRepository.getByCreatedByAndId(email, id);
        installation.setStatus(InstallationSelectionStatus.ARCHIVE);
        return showInstallations(email);
    }

    @Override
    public List<InstallationDTO> deleteInstallationAdmin(int id) {
        installationRepository.deleteById(id);
        return showAllInstallations();
    }

    @Override
    public List<InstallationDTO> copyInstallation(String email, int id) {

        InstallationToSelection installation = entityManager.find(InstallationToSelection.class, id);
        if (installation.getStatus().equals(InstallationSelectionStatus.BUILDER) || installation.getStatus().equals(InstallationSelectionStatus.AIR_APPLIED)){
            throw new LogicalException("Нельзя копировать не посчитанную установку");
        }

        if (installation.getStatus().equals(InstallationSelectionStatus.CALCULATED) ||
                installation.getStatus().equals(InstallationSelectionStatus.DONE) ||
                installation.getStatus().equals(InstallationSelectionStatus.ARCHIVE)) {

            entityManager.detach(installation);
            installation.setId(null);

            InstallationToSelection savedInstallation = installationRepository.save(installation);
            InstallationToSelection calculatedInstallation = selectionService.calculateInstallation(savedInstallation.getId(), savedInstallation);
            installationRepository.save(calculatedInstallation);
            entityManager.detach(calculatedInstallation);

            return showInstallations(email);
        } throw new LogicalException("Нельзя копировать не посчитанную установку");
    }

    @Override
    public InstallationToSelection editInstallation(int id) {

        return installationRepository.findById(id).orElseThrow(new LogicalException("Нет установки с таким id"));
    }

    private List<InstallationDTO> buildInstallations(List<InstallationToSelection> installationsDB) {

        List<InstallationDTO> installationDTOs = new ArrayList<>();

        for (InstallationToSelection installationDB : installationsDB) {
            installationDTOs.add(new InstallationDTO().toBuilder().
                    id(installationDB.getId()).
                    name(installationDB.getName()).
                    calculated(installationDB.getCreatedBy()).
                    createdDate(installationDB.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).
                    status(installationDB.getStatus()).
                    consumption(installationDB.getInflowConsumption() + " / " + installationDB.getOutflowConsumption()).
                    build());
        }
        return installationDTOs;
    }
}
