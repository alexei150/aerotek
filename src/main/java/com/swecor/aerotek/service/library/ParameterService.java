package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.parameter.ParameterRequestDTO;

import java.util.List;

public interface ParameterService {

    Parameter createParameter(ParameterRequestDTO parameterRequestDTO);

    Parameter getParameter(int id);

    List<Parameter> showParameters();

    Parameter updateParameter(ParameterRequestDTO parameterRequestDTO);

    void deleteParameter(int id);
}
