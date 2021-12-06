package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.library.elementType.ElementType;
import com.swecor.aerotek.model.library.elementType.ElementTypeParametersResponse;
import com.swecor.aerotek.model.library.elementType.ElementTypeRequestDTO;

import java.util.List;

public interface ElementTypeService {

    ElementType createElementType(ElementTypeRequestDTO elementTypeRequestDTO);

    ElementType getElementType(int id);

    List<ElementType> showElementTypes();

    ElementType updateElementType(ElementTypeRequestDTO elementTypeRequestDTO);

    void deleteElementType(int id);

    ElementTypeParametersResponse getElementTypeParameters(int id);

}
