package com.swecor.aerotek.rest.controllers.library;

import com.swecor.aerotek.model.library.sectionType.SectionType;
import com.swecor.aerotek.model.library.sectionType.ConstructedSectionTypeResponse;
import com.swecor.aerotek.model.library.sectionType.SectionTypeRequestDTO;
import com.swecor.aerotek.service.library.SectionTypeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/library/section-type")
public class SectionTypeControllerV1 {

    private final SectionTypeService sectionTypeService;

    public SectionTypeControllerV1(@Qualifier("sectionTypeServiceImpl") SectionTypeService sectionTypeService) {
        this.sectionTypeService = sectionTypeService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public SectionType createSectionType(@Valid @RequestBody SectionTypeRequestDTO sectionTypeRequestDTO){
         return sectionTypeService.creatSectionType(sectionTypeRequestDTO);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public SectionType getSectionType(@RequestParam(value = "id") int id) {
        return sectionTypeService.getSectionType(id);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public List<SectionType> showSectionTypes() {
        return sectionTypeService.showSectionTypes();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public SectionType updateSectionType(@Valid @RequestBody SectionTypeRequestDTO sectionTypeRequestDTO){
        return sectionTypeService.updateSectionType(sectionTypeRequestDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteSectionType (@RequestParam(value = "id") int id){
        sectionTypeService.deleteSectionType(id);
    }

    @GetMapping("/get-constructed-section-type")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ConstructedSectionTypeResponse getConstructedSectionType (@RequestParam(value = "id") int id) {
        return sectionTypeService.getConstructedSectionType(id);
    }

    @GetMapping("/show-constructed-section-types")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public List<ConstructedSectionTypeResponse> showConstructedSectionTypes() {
        return sectionTypeService.getAllConstructedSectionTypes();
    }
}
