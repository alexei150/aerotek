package com.swecor.aerotek.rest.controllers.library;

import com.swecor.aerotek.model.library.section.ConstructedSectionResponse;
import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.model.library.section.SectionRequestDTO;
import com.swecor.aerotek.service.library.SectionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/library/section")
public class SectionControllerV1 {

    private final SectionService sectionService;

    public SectionControllerV1(@Qualifier("sectionServiceImpl") SectionService sectionService) {
        this.sectionService = sectionService;
    }


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Section createSection(@Valid @RequestBody SectionRequestDTO sectionRequestDTO) {
        return sectionService.createSection(sectionRequestDTO);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Section getSection(@RequestParam(value = "id") int id) {
        return sectionService.getSection(id);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public List<Section> showSections() {
        return sectionService.showSections();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Section updateSection(@Valid @RequestBody SectionRequestDTO sectionRequestDTO) {
        return sectionService.updateSection(sectionRequestDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteSection(@RequestParam(value = "id") int id) {
        sectionService.deleteSection(id);
    }

    @GetMapping("/get-constructed-section")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ConstructedSectionResponse getConstructedSection(@RequestParam(value = "id") int id) {
        return sectionService.getConstructedSection(id);
    }
}
