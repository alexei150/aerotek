package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.model.library.element.ParameterValueForResponse;
import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.parameterValue.ParameterValue;
import com.swecor.aerotek.model.library.section.Section;
import com.swecor.aerotek.persist.library.ParameterRepository;
import com.swecor.aerotek.persist.library.ParameterValueRepository;
import com.swecor.aerotek.rest.exceptions.library.ParameterIsAbsent;
import com.swecor.aerotek.service.library.ParameterValueService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ParameterValueServiceImpl implements ParameterValueService {

    private final ParameterValueRepository parameterValueRepository;

    private final ParameterRepository parameterRepository;

    public ParameterValueServiceImpl(ParameterValueRepository parameterValueRepository, ParameterRepository parameterRepository) {
        this.parameterValueRepository = parameterValueRepository;
        this.parameterRepository = parameterRepository;
    }

    @Override
    public Set<ParameterValue> createParametersValues(Map<Integer, String> parametersValuesMap) {
        Set<ParameterValue> parametersValuesSet = new HashSet<>();

        for (Map.Entry<Integer, String> entry : parametersValuesMap.entrySet()) {

            parametersValuesSet.add(parameterValueRepository.save(new ParameterValue().toBuilder().
                    parameter(parameterRepository.findById(entry.getKey()).orElseThrow(ParameterIsAbsent::new)).
                    value(entry.getValue()).
                    build()
            ));
        }
        return parametersValuesSet;
    }

    @Override
    public List<ParameterValueForResponse> buildParametersValue (Set<ParameterValue> parametersValuesSet){
        List<ParameterValueForResponse> elementParametersValuesForResponse = new ArrayList<>();

        for (ParameterValue parameterValue: parametersValuesSet){
            elementParametersValuesForResponse.add(
                    new ParameterValueForResponse().toBuilder().
                            id(parameterValue.getParameter().getId()).
                            name(parameterValue.getParameter().getName()).
                            value(parameterValue.getValue()).
                            unit(parameterValue.getParameter().getUnit()).
                            build());
        }
        return  elementParametersValuesForResponse;
    }

    @Override
    public void deleteParametersValuesElement(Set<Parameter> parametersDB, List<Integer> parametersRequestList, Set<Element> elements) {
        //переданные в запросе параметры
        Set<Parameter> parametersRequest = new HashSet<>();

        for (int parameterId : parametersRequestList){
            parametersRequest.add(parameterRepository.findById(parameterId).orElseThrow(ParameterIsAbsent::new));
        }

        //параметры значения которых надо удалить в элементах
        Collection<Parameter> deletedParameters = CollectionUtils.subtract(parametersDB, parametersRequest);
        //получаем все элементы данного типа
        if (!deletedParameters.isEmpty()) {
            for (Element element : elements) {
                //удаляем в каждом элементе лишние значения параметров
                for (Parameter parameter : deletedParameters) {
                    parameterValueRepository.deleteByParameterAndElement(parameter, element);
                }
            }
        }
    }

    @Override
    public void deleteParametersValuesSection(Set<Parameter> parametersDB, List<Integer> parametersRequestList, Set<Section> sections){
        //переданные в запросе параметры
        Set<Parameter> parametersRequest = new HashSet<>();

        for (int parameterId : parametersRequestList){
            parametersRequest.add(parameterRepository.findById(parameterId).orElseThrow(ParameterIsAbsent::new));
        }

        //параметры значения которых надо удалить в секциях
        Collection<Parameter> deletedParameters = CollectionUtils.subtract(parametersDB, parametersRequest);

        //получаем все секции данного типа
        if (!deletedParameters.isEmpty()) {
            for (Section section : sections) {
                //удаляем в каждой секции лишние значения параметров
                for (Parameter parameter : deletedParameters) {
                    parameterValueRepository.deleteByParameterAndSection(parameter, section);
                }
            }
        }
    }
}
