package com.swecor.aerotek.rest.controllers.library;

import com.swecor.aerotek.model.library.elementType.ElementType;
import com.swecor.aerotek.model.library.elementType.ElementTypeParametersResponse;
import com.swecor.aerotek.model.library.elementType.ElementTypeRequestDTO;
import com.swecor.aerotek.service.library.ElementTypeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/library/element-type")
public class ElementTypeControllerV1 {

    private final ElementTypeService elementTypeService;

    public ElementTypeControllerV1(@Qualifier("elementTypeServiceImpl") ElementTypeService elementTypeService) {
        this.elementTypeService = elementTypeService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ElementType createElementType(@Valid @RequestBody ElementTypeRequestDTO elementTypeRequestDTO){
        return elementTypeService.createElementType(elementTypeRequestDTO);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ElementType getElementType(@RequestParam(value = "id") int id) {
        return elementTypeService.getElementType(id);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public List<ElementType> showElementTypes() {
        return elementTypeService.showElementTypes();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ElementType updateElementType(@Valid @RequestBody ElementTypeRequestDTO elementTypeRequestDTO){
        return elementTypeService.updateElementType(elementTypeRequestDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteElementType (@RequestParam(value = "id") int id){
        elementTypeService.deleteElementType(id);
    }

    @GetMapping("/get-parameters")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public ElementTypeParametersResponse getElementTypeParameters (@RequestParam(value = "id") int id) {
        return elementTypeService.getElementTypeParameters(id);
    }
}
