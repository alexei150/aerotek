package com.swecor.aerotek.rest.controllers.selection;

import com.swecor.aerotek.model.selection.Instalation.AirCharacteristicDTO;
import com.swecor.aerotek.model.selection.Instalation.builder.InstallationBuilderDTO;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.service.selection.SelectionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/selection-of-sections")
public class SelectionOfSectionsControllerV1 {

    private final SelectionService selectionService;

    public SelectionOfSectionsControllerV1(@Qualifier("selectionServiceImpl") SelectionService selectionService) {
        this.selectionService = selectionService;
    }

    @PostMapping("/builder")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public InstallationToSelection buildInstallation(@RequestBody InstallationBuilderDTO installationRequest) {
        return selectionService.saveInstallationBuilder(installationRequest);
    }

    @PostMapping("/{id}/builder")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public InstallationToSelection updateBuilder(@PathVariable("id") Integer id, @RequestBody InstallationBuilderDTO installationBuilderRequest) {
        return selectionService.updateInstallationBuilder(id, installationBuilderRequest);
    }

    @PostMapping("/{id}/air-characteristic")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public InstallationToSelection updateAirCharacteristic(@RequestBody AirCharacteristicDTO airCharacteristicDTO, @PathVariable("id") Integer id) {
        return selectionService.updateAirCharacteristic(id, airCharacteristicDTO);
    }

    @PostMapping("/{id}/input-parameters")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public InstallationToSelection getFanCharacteristics(@RequestBody InstallationToSelection installationRequest, @PathVariable("id") int id) {
        return selectionService.calculateInstallation(id, installationRequest);
    }

    @PostMapping("/{id}/save-installation")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public InstallationToSelection saveInstallation(@RequestBody InstallationToSelection installationRequest, @PathVariable("id") int id){
        return selectionService.saveInstallation(id, installationRequest);
    }
}
