package com.swecor.aerotek.rest.controllers.crm;

import com.swecor.aerotek.model.crm.ProjectDTO;
import com.swecor.aerotek.service.crm.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/crm/project")
public class ProjectControllerV1 {

    private final ProjectService projectService;

    public ProjectControllerV1(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(projectService.createProject(authentication.getName(), projectDTO));
    }

    @GetMapping(value = "/{id}/get")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> getProject(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(projectService.getProject(authentication.getName(), id));
    }

    @GetMapping(value = "/show")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> showProject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(projectService.showProjects(authentication.getName()));
    }

    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> updateProject(@RequestBody ProjectDTO projectDTO) {

        return ResponseEntity.ok(projectService.updateProject(projectDTO));
    }

    @DeleteMapping(value = "/{id}/delete")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> deleteProject(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(projectService.deleteProject(authentication.getName(), id));
    }
}
