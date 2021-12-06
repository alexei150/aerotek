package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.selection.integration.heaterExchanger.HeatExchangerDTO;
import com.swecor.aerotek.model.selection.integration.heaterExchanger.RoenEstHeatExchanger;
import com.swecor.aerotek.persist.selction.RoenEstHeatExchangerRepository;
import com.swecor.aerotek.rest.exceptions.LogicalException;
import com.swecor.aerotek.service.library.HeatExchangerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HeatExchangerServiceImpl implements HeatExchangerService {

    private final RoenEstHeatExchangerRepository heatExchangerRepository;

    public HeatExchangerServiceImpl(RoenEstHeatExchangerRepository heatExchangerRepository) {
        this.heatExchangerRepository = heatExchangerRepository;
    }


    @Override
    public RoenEstHeatExchanger createHeatExchanger(HeatExchangerDTO heatExchangerRequestDTO) {

        RoenEstHeatExchanger heatExchanger = new RoenEstHeatExchanger().toBuilder()
                .mode(heatExchangerRequestDTO.getMode())
                .code(heatExchangerRequestDTO.getCode())
                .geometry(heatExchangerRequestDTO.getGeometry())
                .length(heatExchangerRequestDTO.getLength())
                .height(heatExchangerRequestDTO.getHeight())
                .numRows(heatExchangerRequestDTO.getNumRows())
                .tubesType(heatExchangerRequestDTO.getTubesType())
                .finSpacing(heatExchangerRequestDTO.getFinSpacing())
                .finType(heatExchangerRequestDTO.getFinType())
                .circuitsType(heatExchangerRequestDTO.getCircuitsType())
                .numCircuits(heatExchangerRequestDTO.getNumCircuits())
                .headerConfiguration(heatExchangerRequestDTO.getHeaderConfiguration())
                .headerValue1(heatExchangerRequestDTO.getHeaderValue1())
                .headerValue2(heatExchangerRequestDTO.getHeaderValue2())
                .build();

        return heatExchangerRepository.save(heatExchanger);
    }

    @Override
    public RoenEstHeatExchanger getHeatExchanger(int id) {
        return heatExchangerRepository.findById(id).orElseThrow(new LogicalException("В базе данных отсутствуют входные данные для теплообменника RoenEst с таким ID"));
    }

    @Override
    public List<RoenEstHeatExchanger> showHeatExchanger() {
        return heatExchangerRepository.findAll();
    }

    @Override
    public RoenEstHeatExchanger updateHeatExchanger(HeatExchangerDTO heatExchangerRequestDTO) {

        RoenEstHeatExchanger heatExchanger = heatExchangerRepository.findById(heatExchangerRequestDTO.getId()).orElseThrow(
                new LogicalException("В базе данных отсутствуют входные данные для теплообменника RoenEst с таким ID"))
                .toBuilder()
                .mode(heatExchangerRequestDTO.getMode())
                .code(heatExchangerRequestDTO.getCode())
                .geometry(heatExchangerRequestDTO.getGeometry())
                .length(heatExchangerRequestDTO.getLength())
                .height(heatExchangerRequestDTO.getHeight())
                .numRows(heatExchangerRequestDTO.getNumRows())
                .tubesType(heatExchangerRequestDTO.getTubesType())
                .finSpacing(heatExchangerRequestDTO.getFinSpacing())
                .finType(heatExchangerRequestDTO.getFinType())
                .circuitsType(heatExchangerRequestDTO.getCircuitsType())
                .numCircuits(heatExchangerRequestDTO.getNumCircuits())
                .headerConfiguration(heatExchangerRequestDTO.getHeaderConfiguration())
                .headerValue1(heatExchangerRequestDTO.getHeaderValue1())
                .headerValue2(heatExchangerRequestDTO.getHeaderValue2())
                .build();

        return heatExchangerRepository.save(heatExchanger);
    }

    @Override
    public void deleteHeatExchanger(int id) {
        heatExchangerRepository.findById(id).orElseThrow(new LogicalException("В базе данных отсутствуют входные данные для теплообменника RoenEst с таким ID"));

        heatExchangerRepository.deleteById(id);
    }
}
