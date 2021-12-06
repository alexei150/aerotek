package com.swecor.aerotek.service.crm;

import com.swecor.aerotek.model.crm.ProjectDTO;

import java.util.List;

public interface ProjectService {

    ProjectDTO createProject(String email, ProjectDTO projectDTO);

    ProjectDTO getProject(String email, int id);

    List<ProjectDTO> showProjects(String email);

    ProjectDTO updateProject(ProjectDTO projectDTO);

    List<ProjectDTO> deleteProject(String email, int id);
}
