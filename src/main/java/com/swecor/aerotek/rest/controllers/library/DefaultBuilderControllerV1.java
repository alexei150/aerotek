package com.swecor.aerotek.rest.controllers.library;

import com.swecor.aerotek.model.library.builder.DefaultBuilder;
import com.swecor.aerotek.service.library.DefaultBuilderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/library/default-builder")
public class DefaultBuilderControllerV1 {

    private final DefaultBuilderService builderService;

    public DefaultBuilderControllerV1(DefaultBuilderService builderService) {
        this.builderService = builderService;
    }


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public DefaultBuilder createBuilder(@Valid @RequestBody DefaultBuilder builder) {
        return builderService.createBuilder(builder);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public DefaultBuilder getBuilder(@RequestParam(value = "id") int id) {
        return builderService.getBuilder(id);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public List<DefaultBuilder> showBuilders() {
        return builderService.showBuilders();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public DefaultBuilder updateBuilder(@Valid @RequestBody DefaultBuilder builder) {
        return builderService.updateBuilder(builder);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteBuilder(@RequestParam(value = "id") int id) {
        builderService.deleteBuilder(id);
    }

}
