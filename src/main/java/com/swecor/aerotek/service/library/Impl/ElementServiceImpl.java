package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.persist.library.ElementRepository;
import com.swecor.aerotek.persist.library.ElementTypeRepository;
import com.swecor.aerotek.model.library.element.ElementParametersValuesResponse;
import com.swecor.aerotek.model.library.element.ElementRequestDTO;
import com.swecor.aerotek.rest.exceptions.library.ElementDrawingCodeIsNotUnique;
import com.swecor.aerotek.rest.exceptions.library.ElementIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.ElementTypeIsAbsent;
import com.swecor.aerotek.service.library.ElementService;
import com.swecor.aerotek.service.library.ParameterValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ElementServiceImpl implements ElementService {

    private final ElementRepository elementRepository;

    private final ElementTypeRepository elementTypeRepository;

    private final ParameterValueService parameterValueService;

    public ElementServiceImpl(ElementRepository elementRepository, ElementTypeRepository elementTypeRepository, ParameterValueService parameterValueService) {
        this.elementRepository = elementRepository;
        this.elementTypeRepository = elementTypeRepository;
        this.parameterValueService = parameterValueService;
    }

    //todo тут и в секции нужно добавить проверку на наличие параметра в типе элемента
    @Override
    public Element createElement(ElementRequestDTO elementRequestDTO) {
        //проверка уникальности кода чертежа
        if (elementRequestDTO.getDrawingCode() != null && elementRepository.findByDrawingCode(elementRequestDTO.getDrawingCode()).size() > 0){
            throw new ElementDrawingCodeIsNotUnique();
        }

            Element element = new Element().toBuilder().
                    name(elementRequestDTO.getName()).
                    brand(elementRequestDTO.getBrand()).
                    code(elementRequestDTO.getCode()).
                    index(elementRequestDTO.getIndex()).
                    costPrice(elementRequestDTO.getCostPrice()).
                    description(elementRequestDTO.getDescription()).
                    note(elementRequestDTO.getNote()).
                    drawingCode(elementRequestDTO.getDrawingCode()).
                    elementType(elementTypeRepository.findById(elementRequestDTO.getElementTypeId()).orElseThrow(ElementTypeIsAbsent::new)).
                    parametersValues(parameterValueService.createParametersValues(elementRequestDTO.getParametersValues())).
                    build();

            return elementRepository.save(element);
    }

    @Override
    public Element getElement(int id) {
        return elementRepository.findById(id).orElseThrow(ElementIsAbsent::new);
    }

    @Override
    public List<Element> showElements() {
        return elementRepository.findAll();
    }

    @Override
    public Element updateElement(ElementRequestDTO elementRequestDTO) {

        //проверяем на совпадение уникальные коды чертежа с уже имеющимися не нулевыми
        List<Element> elementForDrawingCodeDB = elementRepository.findByDrawingCode(elementRequestDTO.getDrawingCode());
        if (elementRequestDTO.getDrawingCode() != null &&
                !elementForDrawingCodeDB.isEmpty() &&
                !elementForDrawingCodeDB.get(0).getId().equals(elementRequestDTO.getId())){
            throw new ElementDrawingCodeIsNotUnique();
        }

        Element element = elementRepository.findById(elementRequestDTO.getId()).orElseThrow(ElementIsAbsent::new);

        Element updatedElement = element.toBuilder().
                name(elementRequestDTO.getName()).
                brand(elementRequestDTO.getBrand()).
                code(elementRequestDTO.getCode()).
                index(elementRequestDTO.getIndex()).
                costPrice(elementRequestDTO.getCostPrice()).
                description(elementRequestDTO.getDescription()).
                note(elementRequestDTO.getNote()).
                drawingCode(elementRequestDTO.getDrawingCode()).
                elementType(elementTypeRepository.findById(elementRequestDTO.getElementTypeId()).orElseThrow(ElementTypeIsAbsent::new)).
                parametersValues(parameterValueService.createParametersValues(elementRequestDTO.getParametersValues())).
                build();

        return elementRepository.save(updatedElement);

    }

    @Override
    public void deleteElement(int id) {
        elementRepository.findById(id).orElseThrow(ElementIsAbsent::new);

        elementRepository.deleteById(id);
    }

    @Override
    public ElementParametersValuesResponse getElementParametersValues(int id) {

        Element elementDB = elementRepository.findById(id).orElseThrow(ElementIsAbsent::new);

        return  new ElementParametersValuesResponse().toBuilder().
                id(elementDB.getId()).
                name(elementDB.getName()).
                brand(elementDB.getBrand()).
                code(elementDB.getCode()).
                index(elementDB.getIndex()).
                costPrice(elementDB.getCostPrice()).
                description(elementDB.getDescription()).
                note(elementDB.getNote()).
                drawingCode(elementDB.getDrawingCode()).
                elementTypeId(elementDB.getElementType().getId()).
                elementTypeName(elementDB.getElementType().getName()).
                parametersValues(parameterValueService.buildParametersValue(elementDB.getParametersValues())).
                build();
    }
}