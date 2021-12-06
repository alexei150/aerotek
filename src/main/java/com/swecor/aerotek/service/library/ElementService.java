package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.library.element.Element;
import com.swecor.aerotek.model.library.element.ElementParametersValuesResponse;
import com.swecor.aerotek.model.library.element.ElementRequestDTO;

import java.util.List;

public interface ElementService {

    Element createElement(ElementRequestDTO elementRequestDTO);

    Element getElement(int id);

    List<Element> showElements();

    Element updateElement(ElementRequestDTO elementRequestDTO);

    void deleteElement(int id);

    ElementParametersValuesResponse getElementParametersValues(int id);

}
