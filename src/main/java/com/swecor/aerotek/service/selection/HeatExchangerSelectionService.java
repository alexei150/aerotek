package com.swecor.aerotek.service.selection;

import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;

//По standard_size и section type id получаем отсортированный список code теплообменника
//Перебераем index
    // -> по каждому index, standard_size и section_type_id вытягиваем из бд входные параметры для dll
    // -> направляем  соедененные входные данные введенные пользователем и полученные из бд, в dll
    // -> если получаем код ответа RECALC_NO_ERROR, то вставляем параметры response dll  в посчитанную секцию теплообменника
    // -> иначе позвращаем на фронт код ответа
public interface HeatExchangerSelectionService {

    SectionToConfigurator selectHeatExchanger(int flowConsumption, byte standardSize, SectionToConfigurator sectionToConfigurator);
}
