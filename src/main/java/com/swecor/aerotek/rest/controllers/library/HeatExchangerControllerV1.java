package com.swecor.aerotek.rest.controllers.library;

import com.swecor.aerotek.model.selection.integration.heaterExchanger.HeatExchangerDTO;
import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstHeatExchanger;
import com.swecor.aerotek.service.library.HeatExchangerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/library/heat-exchanger")
public class HeatExchangerControllerV1 {

    private final HeatExchangerService heatExchangerService;

    public HeatExchangerControllerV1(@Qualifier("heatExchangerServiceImpl") HeatExchangerService heatExchangerService) {
        this.heatExchangerService = heatExchangerService;
    }


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public RoenEstHeatExchanger createHeatExchanger(@Valid @RequestBody HeatExchangerDTO heatExchangerDTO) {
        return heatExchangerService.createHeatExchanger(heatExchangerDTO);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public RoenEstHeatExchanger getHeatExchanger(@RequestParam(value = "id") int id) {
        return heatExchangerService.getHeatExchanger(id);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public List<RoenEstHeatExchanger> showHeatExchanger() {
        return heatExchangerService.showHeatExchanger();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public RoenEstHeatExchanger updateHeatExchanger(@Valid @RequestBody HeatExchangerDTO heatExchangerDTO) {
        return heatExchangerService.updateHeatExchanger(heatExchangerDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteHeatExchanger(@RequestParam(value = "id") int id) {
        heatExchangerService.deleteHeatExchanger(id);
    }
}
