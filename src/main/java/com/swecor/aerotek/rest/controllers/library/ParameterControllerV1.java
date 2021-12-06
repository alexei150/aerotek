package com.swecor.aerotek.rest.controllers.library;

import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.parameter.ParameterRequestDTO;
import com.swecor.aerotek.service.library.Impl.ParameterServiceImpl;
import com.swecor.aerotek.service.library.ParameterService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/library/parameter")
public class ParameterControllerV1 {

    private final ParameterService parameterService;

    public ParameterControllerV1(@Qualifier("parameterServiceImpl") ParameterServiceImpl parameterService) {
        this.parameterService = parameterService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Parameter createParameter(@Valid @RequestBody ParameterRequestDTO parameterRequestDTO) {
        return parameterService.createParameter(parameterRequestDTO);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Parameter getParameter(@RequestParam(value = "id") int id) {
        return parameterService.getParameter(id);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public List<Parameter> showParameters() {
        return parameterService.showParameters();
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public Parameter updateParameter(@Valid @RequestBody ParameterRequestDTO parameterRequestDTO) {
        return parameterService.updateParameter(parameterRequestDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:write')")
    public void deleteParameter(@RequestParam(value = "id") int id) {
       parameterService.deleteParameter(id);
    }
}
