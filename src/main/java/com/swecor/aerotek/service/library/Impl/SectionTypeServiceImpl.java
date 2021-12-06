package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.model.library.sectionType.SectionType;
import com.swecor.aerotek.model.media.MediaContent;
import com.swecor.aerotek.model.media.SectionTypeIcon;
import com.swecor.aerotek.persist.library.ParameterRepository;
import com.swecor.aerotek.persist.library.SectionTypeRepository;
import com.swecor.aerotek.persist.media.MediaContentRepository;
import com.swecor.aerotek.persist.media.SectionTypeIconRepository;
import com.swecor.aerotek.rest.exceptions.media.FileIsMissingFromTheHardDisk;
import com.swecor.aerotek.rest.exceptions.media.MediaContentIdIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.ParameterIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.SectionTypeIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.SectionTypeIsNotUnique;
import com.swecor.aerotek.model.library.sectionType.ConstructedSectionTypeResponse;
import com.swecor.aerotek.model.library.sectionType.SectionTypeRequestDTO;
import com.swecor.aerotek.service.library.ParameterValueService;
import com.swecor.aerotek.service.library.SectionTypeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.File;
import java.util.*;

@Service
@Transactional
public class SectionTypeServiceImpl implements SectionTypeService {

    private final SectionTypeRepository sectionTypeRepository;

    private final ParameterRepository parameterRepository;

    private final ParameterValueService parameterValueService;

    private final MediaContentRepository mediaContentRepository;

    private final SectionTypeIconRepository sectionTypeIconRepository;

    @Value("${upload.path}")
    private String propertyPath;

    public SectionTypeServiceImpl(SectionTypeRepository sectionTypeRepository, ParameterRepository parameterRepository, ParameterValueService parameterValueService, SectionTypeIconRepository sectionTypeIconRepository, MediaContentRepository mediaContentRepository, SectionTypeIconRepository sectionTypeIconRepository1) {
        this.sectionTypeRepository = sectionTypeRepository;
        this.parameterRepository = parameterRepository;
        this.parameterValueService = parameterValueService;
        this.mediaContentRepository = mediaContentRepository;
        this.sectionTypeIconRepository = sectionTypeIconRepository1;
    }

    @Override
    public SectionType creatSectionType(SectionTypeRequestDTO sectionTypeRequestDTO) {
        try {
            SectionType sectionType = new SectionType().toBuilder().
                    name(sectionTypeRequestDTO.getName()).
                    code(sectionTypeRequestDTO.getCode()).
                    note(sectionTypeRequestDTO.getNote()).
                    parameters(assignParameter(sectionTypeRequestDTO.getParameters())).
                    build();

            SectionType sectionTypeDB = sectionTypeRepository.save(sectionType);

            assignIcon(sectionTypeRequestDTO, sectionTypeDB);

            return sectionTypeRepository.save(sectionType);
        } catch (DataIntegrityViolationException e) {
            throw new SectionTypeIsNotUnique();
        }
    }

    @Override
    public SectionType getSectionType(int id) {
        return sectionTypeRepository.findById(id).orElseThrow(SectionTypeIsAbsent::new);
    }

    @Override
    public List<SectionType> showSectionTypes() {
        return sectionTypeRepository.findAll();
    }

    //TODO не знаю на сколько это правильная проверка на уникальность, возможно лучше отловить SQLException но у нас сроки) разберись как будет время
    @Override
    public SectionType updateSectionType(SectionTypeRequestDTO sectionTypeRequestDTO) {
        //проверяем на уникальность новый code и name
        List<SectionType> sectionTypeForNameDB = sectionTypeRepository.findByName(sectionTypeRequestDTO.getName());
        List<SectionType> sectionTypeForCodeDB = sectionTypeRepository.findByCode(sectionTypeRequestDTO.getCode());
        if ((!sectionTypeForNameDB.isEmpty() && !sectionTypeForNameDB.get(0).getId().equals(sectionTypeRequestDTO.getId()))
                || (!sectionTypeForCodeDB.isEmpty() && !sectionTypeForCodeDB.get(0).getId().equals(sectionTypeRequestDTO.getId()))) {
            throw new SectionTypeIsNotUnique();
        }

        SectionType sectionTypeDB = sectionTypeRepository.findById(sectionTypeRequestDTO.getId()).orElseThrow(SectionTypeIsAbsent::new);

        //удаляем лишние значения параметров в секциях данного типа секций
        //todo тут есть баг, если создать параметры у секций не привязанные к типу, то он их не удалит из секций, аналогичная ситуация с элементами,надо добавить проверку при создании элементов/секций, на наличие такого параметра у типа
        parameterValueService.deleteParametersValuesSection(
                sectionTypeDB.getParameters(),
                sectionTypeRequestDTO.getParameters(),
                sectionTypeRepository.findById(sectionTypeRequestDTO.getId()).orElseThrow(SectionTypeIsAbsent::new).getSections());

        //обновляем
        SectionType sectionTypeToUpdate = sectionTypeDB.toBuilder().
                name(sectionTypeRequestDTO.getName()).
                code(sectionTypeRequestDTO.getCode()).
                note(sectionTypeRequestDTO.getNote()).
                parameters(assignParameter(sectionTypeRequestDTO.getParameters())).
                build();

        reAssignIcon(sectionTypeRequestDTO, sectionTypeDB);

        return sectionTypeRepository.save(sectionTypeToUpdate);
    }

    @Override
    public void deleteSectionType(int id) {

        Map<MediaContent, File> files = getFiles(sectionTypeRepository.findById(id).orElseThrow(SectionTypeIsAbsent::new).getSectionTypeIcons());

        sectionTypeRepository.deleteById(id);

        deleteIconsFromHardDisk(files);
    }

    @Override
    public ConstructedSectionTypeResponse getConstructedSectionType(int id) {

            return buildConstructedSectionTypeResponse(sectionTypeRepository.findById(id).orElseThrow(SectionTypeIsAbsent::new));
    }

    @Override
    public List<ConstructedSectionTypeResponse> getAllConstructedSectionTypes() {
        List<SectionType> sectionTypes = sectionTypeRepository.findAll();

        List<ConstructedSectionTypeResponse> constructedSectionTypeResponseList = new ArrayList<>();
        for (SectionType sectionType: sectionTypes){
            constructedSectionTypeResponseList.add(buildConstructedSectionTypeResponse(sectionType));
        }

        return constructedSectionTypeResponseList;
    }

    private ConstructedSectionTypeResponse buildConstructedSectionTypeResponse(SectionType sectionType){
        return new ConstructedSectionTypeResponse().
                toBuilder().
                id(sectionType.getId()).
                name(sectionType.getName()).
                code(sectionType.getCode()).
                note(sectionType.getNote()).
                image(sectionType.getSectionTypeIcons().getImage()).
                iconBase(sectionType.getSectionTypeIcons().getIconBase()).
                iconOutflowToLeft(sectionType.getSectionTypeIcons().getIconOutflowToLeft()).
                iconOutflowToRight(sectionType.getSectionTypeIcons().getIconOutflowToRight()).
                iconInflowToLeft(sectionType.getSectionTypeIcons().getIconInflowToLeft()).
                iconInflowToRight(sectionType.getSectionTypeIcons().getIconInflowToRight()).
                parameters(List.copyOf(sectionType.getParameters())).
                build();
    }

    private Set<Parameter> assignParameter(List<Integer> parametersId) {
        Set<Parameter> parameters = new HashSet<>();

        for (int param : parametersId) {
            parameters.add(parameterRepository.findById(param).orElseThrow(ParameterIsAbsent::new));
        }
        return parameters;
    }

    private void assignIcon(SectionTypeRequestDTO sectionTypeRequestDTO, SectionType sectionTypeDB) {

        SectionTypeIcon sectionTypeIcon = new SectionTypeIcon().toBuilder().
                sectionType(sectionTypeDB).
                image(mediaContentRepository.findById(sectionTypeRequestDTO.getImage()).orElseThrow(MediaContentIdIsAbsent::new)).
                iconBase(mediaContentRepository.findById(sectionTypeRequestDTO.getIconBase()).orElseThrow(MediaContentIdIsAbsent::new)).
                iconInflowToRight(mediaContentRepository.findById(sectionTypeRequestDTO.getIconInflowToRight()).orElseThrow(MediaContentIdIsAbsent::new)).
                iconInflowToLeft(mediaContentRepository.findById(sectionTypeRequestDTO.getIconInflowToLeft()).orElseThrow(MediaContentIdIsAbsent::new)).
                iconOutflowToRight(mediaContentRepository.findById(sectionTypeRequestDTO.getIconOutflowToRight()).orElseThrow(MediaContentIdIsAbsent::new)).
                iconOutflowToLeft(mediaContentRepository.findById(sectionTypeRequestDTO.getIconOutflowToLeft()).orElseThrow(MediaContentIdIsAbsent::new)).
                sectionType(sectionTypeDB).
                build();

        sectionTypeIconRepository.save(sectionTypeIcon);
    }

    //проверяем наличие MediaContent, если отсутствует то обновляем
    //Изолированое создание MediaContent, в следствии чего однонаправленная связь
    private void reAssignIcon(SectionTypeRequestDTO sectionTypeRequestDTO, SectionType sectionTypeDB){

        SectionTypeIcon sectionTypeIconDB = sectionTypeDB.getSectionTypeIcons();

        if (sectionTypeIconDB.getImage() == null){
            sectionTypeIconDB.setImage(mediaContentRepository.findById(sectionTypeRequestDTO.getImage()).orElseThrow(MediaContentIdIsAbsent::new));
        }
        if (sectionTypeIconDB.getIconBase() == null){
            sectionTypeIconDB.setIconBase(mediaContentRepository.findById(sectionTypeRequestDTO.getIconBase()).orElseThrow(MediaContentIdIsAbsent::new));
        }
        if (sectionTypeIconDB.getIconOutflowToLeft() == null){
            sectionTypeIconDB.setIconOutflowToLeft(mediaContentRepository.findById(sectionTypeRequestDTO.getIconOutflowToLeft()).orElseThrow(MediaContentIdIsAbsent::new));
        }
        if (sectionTypeIconDB.getIconOutflowToRight() == null){
            sectionTypeIconDB.setIconOutflowToRight(mediaContentRepository.findById(sectionTypeRequestDTO.getIconOutflowToRight()).orElseThrow(MediaContentIdIsAbsent::new));
        }
        if (sectionTypeIconDB.getIconInflowToLeft() == null){
            sectionTypeIconDB.setIconInflowToLeft(mediaContentRepository.findById(sectionTypeRequestDTO.getIconInflowToLeft()).orElseThrow(MediaContentIdIsAbsent::new));
        }
        if (sectionTypeIconDB.getIconInflowToRight() == null){
            sectionTypeIconDB.setIconInflowToRight(mediaContentRepository.findById(sectionTypeRequestDTO.getIconInflowToRight()).orElseThrow(MediaContentIdIsAbsent::new));
        }

        sectionTypeIconRepository.save(sectionTypeIconDB);
    }

    private void deleteIconsFromHardDisk(Map<MediaContent, File> files) {

        for (Map.Entry<MediaContent, File> entry : files.entrySet()) {
                if (entry.getValue().delete()){
                    mediaContentRepository.delete(entry.getKey());
                } else {
                    throw new FileIsMissingFromTheHardDisk();
                }

        }

    }

    private Map<MediaContent, File> getFiles(SectionTypeIcon sectionTypeIcons) {

        Map<MediaContent, File> files = new HashMap<>();

        if (sectionTypeIcons.getImage() != null){files.put(sectionTypeIcons.getImage(), new File(propertyPath + "/" + sectionTypeIcons.getImage().getPath()));}
        if (sectionTypeIcons.getIconBase() != null){files.put(sectionTypeIcons.getIconBase(), new File(propertyPath + "/" + sectionTypeIcons.getIconBase().getPath()));}
        if (sectionTypeIcons.getIconOutflowToLeft() != null){files.put(sectionTypeIcons.getIconOutflowToLeft(), new File(propertyPath + "/" + sectionTypeIcons.getIconOutflowToLeft().getPath()));}
        if (sectionTypeIcons.getIconOutflowToRight() != null){files.put(sectionTypeIcons.getIconOutflowToRight(), new File(propertyPath + "/" + sectionTypeIcons.getIconOutflowToRight().getPath()));}
        if (sectionTypeIcons.getIconInflowToLeft() != null){files.put(sectionTypeIcons.getIconInflowToLeft(), new File(propertyPath + "/" + sectionTypeIcons.getIconInflowToLeft().getPath()));}
        if (sectionTypeIcons.getIconInflowToRight() != null){files.put(sectionTypeIcons.getIconInflowToRight(), new File(propertyPath + "/" + sectionTypeIcons.getIconInflowToRight().getPath()));}

        return files;
    }
}
