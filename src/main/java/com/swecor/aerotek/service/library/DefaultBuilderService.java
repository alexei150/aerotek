package com.swecor.aerotek.service.library;

import com.swecor.aerotek.model.library.builder.DefaultBuilder;

import java.util.List;

public interface DefaultBuilderService {

    DefaultBuilder createBuilder(DefaultBuilder builder);

    DefaultBuilder getBuilder(int id);

    List<DefaultBuilder> showBuilders();

    DefaultBuilder updateBuilder(DefaultBuilder builder);

    void deleteBuilder(int id);
}
