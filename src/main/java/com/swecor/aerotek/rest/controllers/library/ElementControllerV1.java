package com.swecor.aerotek.rest.controllers.library;

import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.model.library.element.ElementParametersValuesResponse;
import com.swecor.aerotek.model.library.element.ElementRequestDTO;
import com.swecor.aerotek.service.library.ElementService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/library/element")
public class ElementControllerV1 {

    private final ElementService elementService;

    public ElementControllerV1(@Qualifier("elementServiceImpl") ElementService elementService) {
        this.elementService = elementService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Element createElement(@Valid @RequestBody ElementRequestDTO elementRequestDTO) {
        return elementService.createElement(elementRequestDTO);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Element getElement(@RequestParam(value = "id") int id) {
        return elementService.getElement(id);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public List<Element> showElements() {
        return elementService.showElements();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Element updateElement(@Valid @RequestBody ElementRequestDTO elementRequestDTO) {
        return elementService.updateElement(elementRequestDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteElement(@RequestParam(value = "id") int id) {
        elementService.deleteElement(id);
    }

    @GetMapping("/get-parameters-values")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ElementParametersValuesResponse getElementParametersValue(@RequestParam(value = "id") int id) {
        return elementService.getElementParametersValues(id);
    }
}
