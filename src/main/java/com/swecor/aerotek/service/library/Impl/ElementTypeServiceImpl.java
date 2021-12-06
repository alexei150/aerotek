package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.library.elementType.ElementType;
import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.persist.library.ElementRepository;
import com.swecor.aerotek.persist.library.ElementTypeRepository;
import com.swecor.aerotek.persist.library.ParameterRepository;
import com.swecor.aerotek.model.library.elementType.ElementTypeParametersResponse;
import com.swecor.aerotek.model.library.elementType.ElementTypeRequestDTO;
import com.swecor.aerotek.rest.exceptions.library.ElementTypeIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.ElementTypeIsNotUnique;
import com.swecor.aerotek.rest.exceptions.library.ParameterIsAbsent;
import com.swecor.aerotek.service.library.ElementTypeService;
import com.swecor.aerotek.service.library.ParameterValueService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ElementTypeServiceImpl implements ElementTypeService {

    private final ElementTypeRepository elementTypeRepository;

    private final ParameterRepository parameterRepository;

    private final ParameterValueService parameterValueService;

    public ElementTypeServiceImpl(ElementTypeRepository elementTypeRepository, ParameterRepository parameterRepository, ParameterValueService parameterValueService, ElementRepository elementRepository) {
        this.elementTypeRepository = elementTypeRepository;
        this.parameterRepository = parameterRepository;
        this.parameterValueService = parameterValueService;
    }

    //todo что то мне подсказывает, что лучше нe ловить Exception, а тупо сделать проверку, нет времени принять концептуальное решение, такая же ситуация во всех сервисах на Create
    @Override
    public ElementType createElementType(ElementTypeRequestDTO elementTypeRequestDTO) {
        try {
            ElementType elementType = new ElementType().toBuilder().
                    name(elementTypeRequestDTO.getName()).
                    note(elementTypeRequestDTO.getNote()).
                    parameters(assignParameter(elementTypeRequestDTO.getParameters())).
                    build();

            return elementTypeRepository.save(elementType);
        } catch (DataIntegrityViolationException e) {
            throw new ElementTypeIsNotUnique();
        }
    }

    @Override
    public ElementType getElementType(int id) {
        return elementTypeRepository.findById(id).orElseThrow(ElementTypeIsAbsent::new);
    }

    @Override
    public List<ElementType> showElementTypes() {
        return elementTypeRepository.findAll();
    }

    @Override
    public ElementType updateElementType(ElementTypeRequestDTO elementTypeRequestDTO) {
        //проверяем на уникальность новый name
        List<ElementType> elementTypeForNameDB = elementTypeRepository.findByName(elementTypeRequestDTO.getName());
        if (!elementTypeForNameDB.isEmpty() && !elementTypeForNameDB.get(0).getId().equals(elementTypeRequestDTO.getId())) {
            throw new ElementTypeIsNotUnique();
        }

        ElementType elementTypeDB = elementTypeRepository.findById(elementTypeRequestDTO.getId()).orElseThrow(ElementTypeIsAbsent::new);

        //удаляем значения параметров в элементах если указано меньше чем было
        parameterValueService.deleteParametersValuesElement(
                elementTypeDB.getParameters(),
                elementTypeRequestDTO.getParameters(),
                elementTypeRepository.findById(elementTypeRequestDTO.getId()).orElseThrow(ElementTypeIsAbsent::new).getElements());

        //обновляем
        ElementType elementTypeToUpdate = elementTypeDB.toBuilder().
                name(elementTypeRequestDTO.getName()).
                note(elementTypeRequestDTO.getNote()).
                parameters(assignParameter(elementTypeRequestDTO.getParameters())).
                build();

        return elementTypeRepository.save(elementTypeToUpdate);
    }

    @Override
    public void deleteElementType(int id) {
        elementTypeRepository.findById(id).orElseThrow(ElementTypeIsAbsent::new);

        elementTypeRepository.deleteById(id);
    }

    @Override
    public ElementTypeParametersResponse getElementTypeParameters(int id) {
        ElementType elementType = elementTypeRepository.findById(id).orElseThrow(ElementTypeIsAbsent::new);

        return new ElementTypeParametersResponse().
                toBuilder().
                id(elementType.getId()).
                name(elementType.getName()).
                note(elementType.getNote()).
                parameters(List.copyOf(elementType.getParameters())).
                build();
    }

    private Set<Parameter> assignParameter(List<Integer> parametersId) {
        Set<Parameter> parameters = new HashSet<>();

        for (int param : parametersId) {
            parameters.add(parameterRepository.findById(param).orElseThrow(ParameterIsAbsent::new));
        }
        return parameters;
    }
}
