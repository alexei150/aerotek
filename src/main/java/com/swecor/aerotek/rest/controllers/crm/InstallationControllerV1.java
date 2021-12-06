package com.swecor.aerotek.rest.controllers.crm;

import com.swecor.aerotek.service.crm.InstallationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/v1/crm/installation")
public class InstallationControllerV1 {

    private final InstallationService installationService;

    public InstallationControllerV1(InstallationService installationService) {
        this.installationService = installationService;
    }

    @GetMapping(value = "/show")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> showInstallation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("aerotek:super")) {
            return ResponseEntity.ok(installationService.showAllInstallations());
        } else if (roles.contains("aerotek:read")) {
            return ResponseEntity.ok(installationService.showInstallations(authentication.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("У пользователя отсутствуют подобранные установки");
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> deleteInstallation(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("aerotek:super")) {
            return ResponseEntity.ok(installationService.deleteInstallationAdmin(id));
        }
        return ResponseEntity.ok(installationService.deleteInstallation(authentication.getName(), id));

    }

    @GetMapping(value = "/{id}/copy")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> copyInstallation(@PathVariable int id) throws IOException, ClassNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(installationService.copyInstallation(authentication.getName(), id));
    }

    @GetMapping(value = "/{id}/edit")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public ResponseEntity<?> editInstallation(@PathVariable int id) {

        return ResponseEntity.ok(installationService.editInstallation(id));
    }


}
