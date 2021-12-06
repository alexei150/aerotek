package com.swecor.aerotek.service.selection.Impl;

import com.swecor.aerotek.model.library.section.VariableElement;
import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggFansRequest;
import com.swecor.aerotek.model.selection.fanSelect.ZiehlAbeggFansResponse;
import com.swecor.aerotek.persist.library.VariableElementRepository;
import com.swecor.aerotek.rest.exceptions.selection.ZiehlAbeggSelectionIsAbsent;
import com.swecor.aerotek.rest.externalApi.ziehlAbegg.ZiehlAbeggClient;
import com.swecor.aerotek.service.selection.FanSelectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.swecor.aerotek.service.Constants.*;

@Service
@Transactional
public class FanSelectionServiceImpl implements FanSelectionService {

    private final ZiehlAbeggClient ziehlAbeggClient;

    private final VariableElementRepository variableElementRepository;

    public FanSelectionServiceImpl(ZiehlAbeggClient ziehlAbeggClient, VariableElementRepository variableElementRepository) {
        this.ziehlAbeggClient = ziehlAbeggClient;
        this.variableElementRepository = variableElementRepository;
    }

    // На текущий момент алгоритм подбора вентилятора следующий:
    // достаем из БД список типов вентиляторов для указанного типоразмера,
    // После изменений Внесенныцх Алексеем в алгоритм подбора список в БД теперь не статический а формируется на уровне билбиотеки при создании секции
    // направляем запрос на API и получаем список подходящих
    // начинаем перебирать полученные от API список и сравнивать с нашими в БД
    // возвращаем в случае первого вхождения, т.к. в БД они расположенны в порядке приоритета
    @Override
    public ZiehlAbeggFansResponse selectFan(byte standardSize, int flowConsumption, double workingPointPressure) {
        List<VariableElement> fanList = variableElementRepository.getByStandardSizeAndSectionTypeIdOrderByOrderKeyAsc(standardSize, FAN_FREE_WHEEL_ID);

        ZiehlAbeggFansRequest fansRequest = new ZiehlAbeggFansRequest();
        fansRequest.setQv(flowConsumption);
        fansRequest.setPsf(workingPointPressure);

        List<ZiehlAbeggFansResponse> selectedFans = ziehlAbeggClient.getFans(fansRequest);

        for (ZiehlAbeggFansResponse response: selectedFans){
            for (VariableElement variableElement: fanList){
                if (response.getType().equals(variableElement.getIndex())){
                    return response;
                }
            }
        }
        throw new ZiehlAbeggSelectionIsAbsent();

    }
}
