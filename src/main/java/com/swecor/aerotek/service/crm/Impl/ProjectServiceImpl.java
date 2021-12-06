package com.swecor.aerotek.service.crm.Impl;

import com.swecor.aerotek.model.crm.InstallationDTO;
import com.swecor.aerotek.model.crm.Project;
import com.swecor.aerotek.model.crm.ProjectDTO;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.persist.crm.ProjectRepository;
import com.swecor.aerotek.persist.selction.InstallationToSelectionRepository;
import com.swecor.aerotek.rest.exceptions.crm.ProjectNotFound;
import com.swecor.aerotek.service.crm.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final InstallationToSelectionRepository installationRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, InstallationToSelectionRepository installationRepository) {
        this.projectRepository = projectRepository;
        this.installationRepository = installationRepository;
    }

    @Override
    public ProjectDTO createProject(String email, ProjectDTO projectDTO) {

        Project projectDB = projectRepository.save(builtProjectDB(projectDTO));

        assignInstallations(projectDB, projectDTO.getInstallationsIDs());

        return builtProjectDTO(projectDB, projectDTO);
    }

    @Override
    public ProjectDTO getProject(String email, int id) {
        Project projectDB = projectRepository.findByCreatedByAndId(email, id);

        if (projectDB == null) {
            throw new ProjectNotFound();
        }

        return builtProjectDTO(projectDB);
    }

    @Override
    public List<ProjectDTO> showProjects(String email) {
        List<Project> projects = projectRepository.findByCreatedBy(email);

        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for (Project project : projects) {
            projectDTOs.add(builtProjectDTO(project));
        }

        return projectDTOs;
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        Project projectDB = projectRepository.findById(projectDTO.getId()).orElseThrow(ProjectNotFound::new);

        projectDB.setName(projectDTO.getName());
        projectDB.setAddress(projectDB.getAddress());
        projectDB.setCompany(projectDB.getCompany());
        projectDB.setManager(projectDB.getManager());

        installationRepository.reassignInstallations(projectDB.getCreatedBy(), projectDB.getId());
        assignInstallations(projectDB, projectDTO.getInstallationsIDs());

        return builtProjectDTO(projectDB, projectDTO);
    }

    @Override
    public List<ProjectDTO> deleteProject(String email, int id) {
        projectRepository.deleteByCreatedByAndId(email, id);
        return showProjects(email);
    }

    private Project builtProjectDB(ProjectDTO projectDTO) {

        return new Project().toBuilder().
                name(projectDTO.getName()).
                address(projectDTO.getAddress()).
                company(projectDTO.getCompany()).
                manager(projectDTO.getManager()).
                build();
    }

    public ProjectDTO builtProjectDTO(Project projectDB, ProjectDTO projectDTO) {

        return new ProjectDTO().toBuilder().
                id(projectDB.getId()).
                name(projectDB.getName()).
                createdDate(projectDB.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).
                address(projectDB.getAddress()).
                company(projectDB.getCompany()).
                manager(projectDB.getManager()).
                userName(projectDB.getCreatedBy()).
                installationsCount(projectDTO.getInstallationsIDs().size()).
                installations(projectDTO.getInstallations()).
                build();
    }

    public ProjectDTO builtProjectDTO(Project projectDB) {

        return new ProjectDTO().toBuilder().
                id(projectDB.getId()).
                name(projectDB.getName()).
                createdDate(projectDB.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).
                address(projectDB.getAddress()).
                company(projectDB.getCompany()).
                manager(projectDB.getManager()).
                userName(projectDB.getCreatedBy()).
                installationsCount(projectDB.getInstallations().size()).
                installations(buildInstallations(new ArrayList<>(projectDB.getInstallations()))).
                build();
    }

    private void assignInstallations(Project projectDB, List<Integer> installationsRequest) {

        for (Integer installationId : installationsRequest) {
            InstallationToSelection installation = installationRepository.getOne(installationId);
            installation.setProject(projectDB);
        }
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
