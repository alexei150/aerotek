package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.library.element.ElementCountResponse;
import com.swecor.aerotek.model.library.section.*;
import com.swecor.aerotek.persist.library.*;
import com.swecor.aerotek.rest.exceptions.library.ElementIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.SectionDrawingCodeIsNotUnique;
import com.swecor.aerotek.rest.exceptions.library.SectionIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.SectionTypeIsAbsent;
import com.swecor.aerotek.persist.library.AcousticPerformanceRepository;
import com.swecor.aerotek.service.library.ParameterValueService;
import com.swecor.aerotek.service.library.SectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    private final SectionTypeRepository sectionTypeRepository;

    private final ElementRepository elementRepository;

    private final ParameterValueService parameterValueService;

    private final SectionElementsCountRepository sectionElementsCountRepository;

    private final AirConsumptionRepository airConsumptionRepository;

    private final AcousticPerformanceRepository acousticPerformanceRepository;

    private final VariableElementRepository variableElementRepository;

    public SectionServiceImpl(SectionRepository sectionRepository, SectionTypeRepository sectionTypeRepository, ElementRepository elementRepository, ParameterValueService parameterValueService, SectionElementsCountRepository sectionElementsCountRepository, AirConsumptionRepository airConsumptionRepository, AcousticPerformanceRepository acousticPerformanceRepository, VariableElementRepository variableElementRepository) {
        this.sectionRepository = sectionRepository;
        this.sectionTypeRepository = sectionTypeRepository;
        this.elementRepository = elementRepository;
        this.parameterValueService = parameterValueService;
        this.sectionElementsCountRepository = sectionElementsCountRepository;
        this.airConsumptionRepository = airConsumptionRepository;
        this.acousticPerformanceRepository = acousticPerformanceRepository;
        this.variableElementRepository = variableElementRepository;
    }

    //todo Возможно есть способ выполнить встаку колличества элементов в каскадную таблицу меньшем колличеством запросов, выглядит как говнокод, разберись как будет время
    //todo почему то hibernate выполняет update parameter_value после всех вставок, разберись как будет время
    @Override
    public Section createSection(SectionRequestDTO sectionRequest) {
        //проверка уникальности кода чертежа
        if (!sectionRepository.findByDrawingCode(sectionRequest.getDrawingCode()).isEmpty()){
            throw new SectionDrawingCodeIsNotUnique();
        }

        Section section = new Section().toBuilder().
                name(sectionRequest.getName()).
                drawingCode(sectionRequest.getDrawingCode()).
                description(sectionRequest.getDescription()).
                note(sectionRequest.getNote()).
                buildCoefficient(sectionRequest.getBuildCoefficient()).
                hardwareCoefficient(sectionRequest.getHardwareCoefficient()).
                sectionArea(sectionRequest.getSectionArea()).
                standardSize(sectionRequest.getStandardSize()).
                sectionType(sectionTypeRepository.findById(sectionRequest.getSectionTypeId()).orElseThrow(SectionTypeIsAbsent::new)).
                parametersValues(parameterValueService.createParametersValues(sectionRequest.getParametersValues())).
                build();

        Section sectionDB = sectionRepository.save(section);

        //назначаем элементы с их колличеством в каскадную таблицу
        assignElementsCount(sectionDB, sectionRequest.getElementsCount());

        //создаем и назначаем расход воздуха
        createAndAssignAirConsumption(sectionDB, sectionRequest.getAirConsumption());

        //создаем и назначаем акустические характеристики
        createAndAssignAcousticPerformance(sectionDB, sectionRequest.getAcousticPerformance());

        //создаем и назначаем вариативные элементы
        createAndAssignVariableElements(sectionDB, sectionRequest);

        return sectionDB;
    }

    @Override
    public Section getSection(int id) {
        return sectionRepository.findById(id).orElseThrow(SectionIsAbsent::new);
    }

    @Override
    public List<Section> showSections() {
        return sectionRepository.findAll();
    }

    //todo как то не очень красиво я выполняю update, возможно есть решение получше
    @Override
    public Section updateSection(SectionRequestDTO sectionRequest) {

        List<Section> sectionForDrawingCodeDB = sectionRepository.findByDrawingCode(sectionRequest.getDrawingCode());
        if (!sectionForDrawingCodeDB.isEmpty() && !sectionForDrawingCodeDB.get(0).getId().equals(sectionRequest.getId())){
            throw new SectionDrawingCodeIsNotUnique();
        }

        Section sectionDB = sectionRepository.findById(sectionRequest.getId()).orElseThrow(SectionIsAbsent::new);

        Section updatedSection = sectionDB.toBuilder().
                name(sectionRequest.getName()).
                drawingCode(sectionRequest.getDrawingCode()).
                description(sectionRequest.getDescription()).
                note(sectionRequest.getNote()).
                buildCoefficient(sectionRequest.getBuildCoefficient()).
                hardwareCoefficient(sectionRequest.getHardwareCoefficient()).
                sectionArea(sectionRequest.getSectionArea()).
                standardSize(sectionRequest.getStandardSize()).
                sectionType(sectionTypeRepository.findById(sectionRequest.getSectionTypeId()).orElseThrow(SectionTypeIsAbsent::new)).
                parametersValues(parameterValueService.createParametersValues(sectionRequest.getParametersValues())).
                build();

        //назначаем элементы с их колличеством в каскадную таблицу
        sectionElementsCountRepository.deleteBySection(sectionDB);
        assignElementsCount(updatedSection, sectionRequest.getElementsCount());

        //назначаем расход воздуха
        airConsumptionRepository.deleteBySection(sectionDB);
        createAndAssignAirConsumption(updatedSection, sectionRequest.getAirConsumption());

        //todo в таком случая нет перескакивания id возможно стоит применять именно такую форму update
        //todo хотя перескакивание id происходит и за того, что не статичные данные, непонятно сколько войдет, приходится пересоздавать
        //обновляем акустические характеристики
        updateAcousticPerformance(updatedSection, sectionRequest.getAcousticPerformance());

        //назначаем вариативные элементы
        variableElementRepository.deleteBySection(sectionDB);
        createAndAssignVariableElements(updatedSection, sectionRequest);

       return sectionRepository.save(updatedSection);
    }

    @Override
    public void deleteSection(int id) {
        sectionRepository.findById(id).orElseThrow(SectionIsAbsent::new);

        sectionRepository.deleteById(id);
    }

    @Override
    public ConstructedSectionResponse getConstructedSection(int id){

        Section sectionDB = sectionRepository.findById(id).orElseThrow(SectionIsAbsent::new);

        return new ConstructedSectionResponse().toBuilder().
                id(sectionDB.getId()).
                name(sectionDB.getName()).
                drawingCode(sectionDB.getDrawingCode()).
                description(sectionDB.getDescription()).
                note(sectionDB.getNote()).
                buildCoefficient(sectionDB.getBuildCoefficient()).
                hardwareCoefficient(sectionDB.getHardwareCoefficient()).
                sectionArea(sectionDB.getSectionArea()).
                standardSize(sectionDB.getStandardSize()).
                sectionType(sectionDB.getSectionType()).
                parametersValues(parameterValueService.buildParametersValue(sectionDB.getParametersValues())).
                elements(buildElementsCount(sectionDB)).
                acousticPerformance(sectionDB.getAcousticPerformance()).
                airConsumptions(buildAirConsumption(sectionDB)).
                variableElements(buildVariableElements(sectionDB)).
                build();
    }

    private void assignElementsCount(Section savedSection, Map<Integer, Integer> elementsCountMap) {

        for (Map.Entry<Integer, Integer> entry : elementsCountMap.entrySet()) {

            sectionElementsCountRepository.save(new SectionElementsCount().toBuilder().
                    section(savedSection).
                    element(elementRepository.findById(entry.getKey()).orElseThrow(ElementIsAbsent::new)).
                    elementsCount(entry.getValue()).
                    build());
        }
    }

    private void createAndAssignAirConsumption(Section section, Map<Integer, Float> airConsumptionRequest) {
        for (Map.Entry<Integer, Float> entry : airConsumptionRequest.entrySet()) {

            airConsumptionRepository.save(new AirConsumption().toBuilder().
                    section(section).
                    consumption(entry.getKey()).
                    pressure(entry.getValue()).
                    build());
        }


    }

    private void createAndAssignAcousticPerformance(Section section, AcousticPerformance acousticPerformance){
        acousticPerformanceRepository.save(new AcousticPerformance().toBuilder().
                section(section).
                hz63(acousticPerformance.getHz63()).
                hz125(acousticPerformance.getHz125()).
                hz250(acousticPerformance.getHz250()).
                hz500(acousticPerformance.getHz500()).
                hz1000(acousticPerformance.getHz1000()).
                hz2000(acousticPerformance.getHz2000()).
                hz4000(acousticPerformance.getHz4000()).
                hz8000(acousticPerformance.getHz8000()).
                build());
    }

    private void createAndAssignVariableElements(Section section, SectionRequestDTO sectionRequest){
        for (Map.Entry<Integer, String> entry :sectionRequest.getVariableElements().entrySet()){

            variableElementRepository.save(new VariableElement().toBuilder().
                    section(section).
                    standardSize(sectionRequest.getStandardSize()).
                    orderKey(entry.getKey()).
                    index(entry.getValue()).
                    sectionTypeId(sectionRequest.getSectionTypeId()).
                    build());
        }

    }

    private void updateAcousticPerformance(Section sectionDB, AcousticPerformance acousticPerformance){
        acousticPerformanceRepository.save(sectionDB.getAcousticPerformance().toBuilder().
                hz63(acousticPerformance.getHz63()).
                hz125(acousticPerformance.getHz125()).
                hz250(acousticPerformance.getHz250()).
                hz500(acousticPerformance.getHz500()).
                hz1000(acousticPerformance.getHz1000()).
                hz2000(acousticPerformance.getHz2000()).
                hz4000(acousticPerformance.getHz4000()).
                hz8000(acousticPerformance.getHz8000()).
                build());
    }

    private List<ElementCountResponse> buildElementsCount(Section section){
        List<ElementCountResponse> elementCountResponseList = new ArrayList<>();

        for (SectionElementsCount sectionElementCount: section.getElements()){
            elementCountResponseList.add(new ElementCountResponse().toBuilder().
                    id(sectionElementCount.getElement().getId()).
                    name(sectionElementCount.getElement().getName()).
                    count(sectionElementCount.getElementsCount()).
                    build());
        }

        return elementCountResponseList;
    }

    private Map<Integer, Float> buildAirConsumption(Section section){
        Map<Integer, Float> airConsumptionMap = new HashMap<>();

        for (AirConsumption airConsumption: section.getAirConsumptions()){
            airConsumptionMap.put(airConsumption.getConsumption(), airConsumption.getPressure());
        }

        return airConsumptionMap;
    }

    private Map<Integer, String> buildVariableElements(Section section){
        Map<Integer, String> variableElementsMap = new HashMap<>();

        for (VariableElement variableElement: section.getVariableElements()){
            variableElementsMap.put(variableElement.getOrderKey(), variableElement.getIndex());
        }

        return variableElementsMap;
    }

}
