package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.model.library.element.ParameterValueForResponse;
import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.parameterValue.ParameterValue;
import com.swecor.aerotek.model.library.section.Section;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface ParameterValueService {

    Set<ParameterValue> createParametersValues(Map<Integer, String> parametersValuesMap);

    List<ParameterValueForResponse> buildParametersValue (Set<ParameterValue> parametersValuesSet);

    void deleteParametersValuesElement(Set<Parameter> parametersDB, List<Integer> parametersRequestList, Set<Element> elements);

    void deleteParametersValuesSection(Set<Parameter> parametersDB, List<Integer> parametersRequestList, Set<Section> sections);

}
