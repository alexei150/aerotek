package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.library.parameter.Parameter;
import com.swecor.aerotek.persist.library.ParameterRepository;
import com.swecor.aerotek.rest.exceptions.library.ParameterIsAbsent;
import com.swecor.aerotek.rest.exceptions.library.ParameterIsNotUnique;
import com.swecor.aerotek.model.library.parameter.ParameterRequestDTO;
import com.swecor.aerotek.service.library.ParameterService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    private final ParameterRepository parameterRepository;

    public ParameterServiceImpl(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    //TODO проблема в перескакивании id при Exception хотя проблема ли это?), возможно стоит делать проверку до попытки вставки, нет времени разобраться пока будет так
    @Override
    public Parameter createParameter(ParameterRequestDTO parameterRequestDTO){
        try {
            Parameter parameter = new Parameter().toBuilder().
                    name(parameterRequestDTO.getName()).
                    minValue(parameterRequestDTO.getMinValue()).
                    maxValue(parameterRequestDTO.getMaxValue()).
                    unit(parameterRequestDTO.getUnit()).
                    build();

            return parameterRepository.save(parameter);
        } catch (DataIntegrityViolationException e) {
            throw new ParameterIsNotUnique();
        }
    }
    @Override
    public Parameter getParameter(int id){
        return parameterRepository.findById(id).orElseThrow(ParameterIsAbsent::new);
    }

    @Override
    public List<Parameter> showParameters() {
        return parameterRepository.findAll();
    }

    @Override
    public Parameter updateParameter(ParameterRequestDTO parameterRequestDTO){
        List<Parameter> parameterDB = parameterRepository.findByName(parameterRequestDTO.getName());
        if (!parameterDB.isEmpty() && !parameterDB.get(0).getId().equals(parameterRequestDTO.getId())){
            throw new ParameterIsNotUnique();
        }

        Parameter parameter =parameterRepository.findById(parameterRequestDTO.getId()).orElseThrow(ParameterIsAbsent::new).
                toBuilder().
                name(parameterRequestDTO.getName()).
                minValue(parameterRequestDTO.getMinValue()).
                maxValue(parameterRequestDTO.getMaxValue()).
                unit(parameterRequestDTO.getUnit()).
                build();

        return parameterRepository.save(parameter);
    }

    @Override
    public void deleteParameter(int id) {
        parameterRepository.findById(id).orElseThrow(ParameterIsAbsent::new);

        parameterRepository.deleteById(id);
    }
}
