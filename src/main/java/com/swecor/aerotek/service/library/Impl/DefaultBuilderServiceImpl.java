package com.swecor.aerotek.service.library.Impl;

import com.swecor.aerotek.model.library.builder.DefaultBuilder;
import com.swecor.aerotek.persist.library.DefaultBuilderRepository;
import com.swecor.aerotek.rest.exceptions.library.DefaultBuilderIsAbsent;
import com.swecor.aerotek.service.library.DefaultBuilderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultBuilderServiceImpl implements DefaultBuilderService {

    private final DefaultBuilderRepository builderRepository;

    public DefaultBuilderServiceImpl(DefaultBuilderRepository builderRepository) {
        this.builderRepository = builderRepository;
    }

    @Override
    public DefaultBuilder createBuilder(DefaultBuilder builder) {
        return builderRepository.save(builder);
    }

    @Override
    public DefaultBuilder getBuilder(int id) {
        return builderRepository.findById(id).orElseThrow(DefaultBuilderIsAbsent::new);
    }

    @Override
    public List<DefaultBuilder> showBuilders() {
        return builderRepository.findAll();
    }

    @Override
    public DefaultBuilder updateBuilder(DefaultBuilder builder) {
        DefaultBuilder builderDB = builderRepository.findById(builder.getId()).orElseThrow(DefaultBuilderIsAbsent::new);

        DefaultBuilder updatedBuilder = builderDB.toBuilder().
                name(builder.getName()).
                description(builder.getDescription()).
                inflow(builder.getInflow()).
                outflow(builder.getOutflow()).
                outflowIsUp(builder.isOutflowIsUp()).
                build();


        return builderRepository.save(updatedBuilder);
    }

    @Override
    public void deleteBuilder(int id) {
        builderRepository.deleteById(id);
    }
}
