package com.swecor.aerotek.service.calculation.Impl;

import com.swecor.aerotek.model.library.section.AcousticPerformance;
import com.swecor.aerotek.model.selection.Instalation.AcousticPerformanceDTO;
import com.swecor.aerotek.model.selection.Instalation.flow.ParameterToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.rest.exceptions.calculation.FanToAcousticCalculateNotFound;
import com.swecor.aerotek.rest.exceptions.calculation.ParseZiehlAbeggApiResponseFailed;
import com.swecor.aerotek.rest.exceptions.calculation.UnselectedThickness;
import com.swecor.aerotek.rest.exceptions.library.SectionIsAbsent;
import com.swecor.aerotek.rest.exceptions.selection.InstallationFlowIsEmpty;
import com.swecor.aerotek.service.calculation.Calculated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.*;

@Service
@Transactional
public class AcousticPerformanceServiceImpl implements Calculated {

    private final SectionRepository sectionRepository;

    public static final AcousticPerformance ACOUSTIC_25 = new AcousticPerformance(-6.0, -8.0, -11.0, -10.0, -9.0, -12.0, -7.0, -5.0);
    public static final AcousticPerformance ACOUSTIC_45 = new AcousticPerformance(-9.0, -11.0, -14.0, -13.0, -12.0, -15.0, -10.0, -8.0);

    public AcousticPerformanceServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public InstallationToSelection calculate(InstallationToSelection installation) {
        List<SectionToConfigurator> inflow = installation.getInflowParameters();
        List<SectionToConfigurator> outflow = installation.getOutflowParameters();

        boolean isInflow = !inflow.isEmpty();
        boolean isOutflow = !outflow.isEmpty();

        List<AcousticPerformanceDTO> acousticPerformanceList = new ArrayList<>();

        if (!isInflow && !isOutflow) {
            throw new InstallationFlowIsEmpty();
        } else if (isInflow && isOutflow) {
            //Приток Всасывание
            acousticPerformanceList.add(getFlowSuction(inflow, getThickness(installation), installation.getBuilder().getInflow().isDirection()));

            //Приток нагнетание
            acousticPerformanceList.add(getFlowInjection(inflow, getThickness(installation), installation.getBuilder().getInflow().isDirection()));

            //Приток к окружению
            acousticPerformanceList.add(getFlowEnvironment(inflow, getThickness(installation)));

            //Вытяжка всасывание
            acousticPerformanceList.add(getFlowSuction(outflow, getThickness(installation), installation.getBuilder().getOutflow().isDirection()));

            //Вытяжка нагнетание
            acousticPerformanceList.add(getFlowInjection(outflow, getThickness(installation), installation.getBuilder().getOutflow().isDirection()));

            //Вытяжка к окружению
            acousticPerformanceList.add(getFlowEnvironment(outflow, getThickness(installation)));
        } else if (isInflow) {
            //Приток Всасывание
            acousticPerformanceList.add(getFlowSuction(inflow, getThickness(installation), installation.getBuilder().getInflow().isDirection()));

            //Приток нагнетание
            acousticPerformanceList.add(getFlowInjection(inflow, getThickness(installation), installation.getBuilder().getInflow().isDirection()));

            //Приток к окружению
            acousticPerformanceList.add(getFlowEnvironment(inflow, getThickness(installation)));
        } else if (isOutflow) {
            //Вытяжка всасывание
            acousticPerformanceList.add(getFlowSuction(outflow, getThickness(installation), installation.getBuilder().getOutflow().isDirection()));

            //Вытяжка нагнетание
            acousticPerformanceList.add(getFlowInjection(outflow, getThickness(installation), installation.getBuilder().getOutflow().isDirection()));

            //Вытяжка к окружению
            acousticPerformanceList.add(getFlowEnvironment(outflow, getThickness(installation)));
        }

        installation.setAcousticTable(acousticPerformanceList);

        return installation;
    }

    @Override
    public int getMySectionTypeId() {
        return 21;
    }

    private AcousticPerformanceDTO getFlowSuction(List<SectionToConfigurator> flowSections, AcousticPerformance acousticThickness, boolean isDirection) {

        //получаем вентилятор
        SectionToConfigurator fan = getFlowFan(flowSections);
        int positionOfFan = fan.getPosition();

        //собираем все секции на всасывании
        List<SectionToConfigurator> suctionSections = getSuctionSections(flowSections, isDirection, positionOfFan);

        //получаем Сумму снижения звука всех шумоглушителей на всасывании
        AcousticPerformance sumMufflers = getSumMufflers(suctionSections);

        //получаем шумовые характеристики вентилятора на всасывании
        double fanAcousticSuction = getFanAcousticSuction(fan);

        //суммируем шумовые характеристики вентилятора и шумоглушителей по частотам
        return AcousticPerformanceDTO.builder().
                hz63(String.format("%.2f", sumMufflers.getHz63() + fanAcousticSuction + acousticThickness.getHz63())).
                hz125(String.format("%.2f", sumMufflers.getHz125() + fanAcousticSuction + acousticThickness.getHz125())).
                hz250(String.format("%.2f", sumMufflers.getHz250() + fanAcousticSuction + acousticThickness.getHz250())).
                hz500(String.format("%.2f", sumMufflers.getHz500() + fanAcousticSuction + acousticThickness.getHz500())).
                hz1000(String.format("%.2f", sumMufflers.getHz1000() + fanAcousticSuction + acousticThickness.getHz1000())).
                hz2000(String.format("%.2f", sumMufflers.getHz2000() + fanAcousticSuction + acousticThickness.getHz2000())).
                hz4000(String.format("%.2f", sumMufflers.getHz4000() + fanAcousticSuction + acousticThickness.getHz4000())).
                hz8000(String.format("%.2f", sumMufflers.getHz8000() + fanAcousticSuction + acousticThickness.getHz8000())).
                sum(String.format("%.2f", fanAcousticSuction)).
                build();
    }

    private AcousticPerformanceDTO getFlowInjection(List<SectionToConfigurator> flowSections, AcousticPerformance acousticThickness, boolean isDirection) {

        //получаем вентилятор
        SectionToConfigurator fan = getFlowFan(flowSections);
        int positionOfFan = fan.getPosition();

        //собираем все секции на нагнетании
        List<SectionToConfigurator> suctionSections = getInjectionSections(flowSections, isDirection, positionOfFan);

        //получаем Сумму снижения звука всех шумоглушителей на нагнетании
        AcousticPerformance sumMufflers = getSumMufflers(suctionSections);

        //получаем шумовые характеристики вентилятора на нагнетании
        double fanAcousticSuction = getFanAcousticInjection(fan);

        //суммируем шумовые характеристики вентилятора и шумоглушителей по частотам
        return AcousticPerformanceDTO.builder().
                hz63(String.format("%.2f", sumMufflers.getHz63() + fanAcousticSuction + acousticThickness.getHz63())).
                hz125(String.format("%.2f", sumMufflers.getHz125() + fanAcousticSuction + acousticThickness.getHz125())).
                hz250(String.format("%.2f", sumMufflers.getHz250() + fanAcousticSuction + acousticThickness.getHz250())).
                hz500(String.format("%.2f", sumMufflers.getHz500() + fanAcousticSuction + acousticThickness.getHz500())).
                hz1000(String.format("%.2f", sumMufflers.getHz1000() + fanAcousticSuction + acousticThickness.getHz1000())).
                hz2000(String.format("%.2f", sumMufflers.getHz2000() + fanAcousticSuction + acousticThickness.getHz2000())).
                hz4000(String.format("%.2f", sumMufflers.getHz4000() + fanAcousticSuction + acousticThickness.getHz4000())).
                hz8000(String.format("%.2f", sumMufflers.getHz8000() + fanAcousticSuction + acousticThickness.getHz8000())).
                sum(String.format("%.2f", fanAcousticSuction)).
                build();
    }

    private AcousticPerformanceDTO getFlowEnvironment(List<SectionToConfigurator> flowSections, AcousticPerformance acousticThickness) {

        double fanAcousticSuction = getFanAcousticInjection(getFlowFan(flowSections));

        //Для точности расчетов оставляем все в double
        AcousticPerformance acousticPerformanceToCalculate = AcousticPerformance.builder().
                hz63(fanAcousticSuction + acousticThickness.getHz125()).
                hz125(fanAcousticSuction + acousticThickness.getHz125()).
                hz250(fanAcousticSuction + acousticThickness.getHz250()).
                hz500(fanAcousticSuction + acousticThickness.getHz500()).
                hz1000(fanAcousticSuction + acousticThickness.getHz1000()).
                hz2000(fanAcousticSuction + acousticThickness.getHz2000()).
                hz4000(fanAcousticSuction + acousticThickness.getHz4000()).
                hz8000(fanAcousticSuction + acousticThickness.getHz8000()).
                build();

        return AcousticPerformanceDTO.builder().
                hz63(String.format("%.2f", acousticPerformanceToCalculate.getHz63())).
                hz125(String.format("%.2f", acousticPerformanceToCalculate.getHz125())).
                hz250(String.format("%.2f", acousticPerformanceToCalculate.getHz250())).
                hz500(String.format("%.2f", acousticPerformanceToCalculate.getHz500())).
                hz1000(String.format("%.2f", acousticPerformanceToCalculate.getHz1000())).
                hz2000(String.format("%.2f", acousticPerformanceToCalculate.getHz2000())).
                hz4000(String.format("%.2f", acousticPerformanceToCalculate.getHz4000())).
                hz8000(String.format("%.2f", acousticPerformanceToCalculate.getHz8000())).
                sum(String.format("%.2f", getGeometricMeanFrequency(acousticPerformanceToCalculate))).
                build();
    }

    private SectionToConfigurator getFlowFan(List<SectionToConfigurator> flowSections) {

        for (SectionToConfigurator section : flowSections) {
            if (section.getSectionType().getId() == 1) {
                return section;
            }
        }
        throw new FanToAcousticCalculateNotFound();

    }

    private List<SectionToConfigurator> getSuctionSections(List<SectionToConfigurator> flowSections, boolean isDirection, int positionOfFan) {
        //собираем все секции на всасывании
        List<SectionToConfigurator> suctionSections = new ArrayList<>();

        //если всасывание слева, то собираем все секции слева от вентилятора
        if (isDirection) {
            for (int i = 1; i < positionOfFan; i++) {
                for (SectionToConfigurator section : flowSections) {
                    if (section.getPosition() == i) {
                        suctionSections.add(section);
                    }
                }
            }
            //если всасывание справа, то собираем все секции справа от вентилятора
        } else {
            for (int i = positionOfFan + 1; i < flowSections.size() + 1; i++) {
                for (SectionToConfigurator section : flowSections) {
                    if (section.getPosition() == i) {
                        suctionSections.add(section);
                    }
                }
            }
        }
        return suctionSections;
    }

    private List<SectionToConfigurator> getInjectionSections(List<SectionToConfigurator> flowSections, boolean isDirection, int positionOfFan) {
        //собираем все секции на всасывании
        List<SectionToConfigurator> injectionSections = new ArrayList<>();

        //если нагнетание слева, то собираем все секции слева от вентилятора
        if (!isDirection) {
            for (int i = 1; i < positionOfFan; i++) {
                for (SectionToConfigurator section : flowSections) {
                    if (section.getPosition() == i) {
                        injectionSections.add(section);
                    }
                }
            }
            //если нагнетание справа, то собираем все секции справа от вентилятора
        } else {
            for (int i = positionOfFan + 1; i < flowSections.size() + 1; i++) {
                for (SectionToConfigurator section : flowSections) {
                    if (section.getPosition() == i) {
                        injectionSections.add(section);
                    }
                }
            }
        }
        return injectionSections;
    }

    private AcousticPerformance getSumMufflers(List<SectionToConfigurator> flowSections) {

        //получаем все шумоглушители
        List<SectionToConfigurator> mufflerList = new ArrayList<>();

        for (SectionToConfigurator section : flowSections) {
            if (section.getSectionType().getId() == 2) {
                mufflerList.add(section);
            }
        }

        //суммируем все их шумовые характеристики
        if (mufflerList.size() == 0) {
            return new AcousticPerformance();
        } else if (mufflerList.size() == 1) {
            return sectionRepository.findById(mufflerList.get(0).getSelectedSectionId()).orElseThrow(SectionIsAbsent::new).getAcousticPerformance();
        } else {
            AcousticPerformance sumMufflers = new AcousticPerformance();

            for (SectionToConfigurator section : mufflerList) {
                AcousticPerformance mufflerAcoustic = sectionRepository.findById(section.getSelectedSectionId()).orElseThrow(SectionIsAbsent::new).getAcousticPerformance();

                sumMufflers.setHz63(sumMufflers.getHz63() + mufflerAcoustic.getHz63());
                sumMufflers.setHz125(sumMufflers.getHz125() + mufflerAcoustic.getHz125());
                sumMufflers.setHz250(sumMufflers.getHz250() + mufflerAcoustic.getHz250());
                sumMufflers.setHz500(sumMufflers.getHz500() + mufflerAcoustic.getHz500());
                sumMufflers.setHz1000(sumMufflers.getHz1000() + mufflerAcoustic.getHz1000());
                sumMufflers.setHz2000(sumMufflers.getHz2000() + mufflerAcoustic.getHz2000());
                sumMufflers.setHz4000(sumMufflers.getHz4000() + mufflerAcoustic.getHz4000());
                sumMufflers.setHz8000(sumMufflers.getHz8000() + mufflerAcoustic.getHz8000());
            }

            return sumMufflers;
        }
    }

    private double getFanAcousticSuction(SectionToConfigurator section) {
        for (ParameterToSelection parameter : section.getCalculated()) {
            if (parameter.getParam().getName().equals("Шумовые характеристики всасывания")) {
                try {
                    return NumberFormat.getInstance(Locale.FRANCE).parse(parameter.getValue()).doubleValue();
                } catch (Exception e) {
                    throw new ParseZiehlAbeggApiResponseFailed();
                }
            }
        }
        return 0;
    }

    private double getFanAcousticInjection(SectionToConfigurator section) {
        for (ParameterToSelection parameter : section.getCalculated()) {
            if (parameter.getParam().getName().equals("Шумовые характеристики нагнетания")) {
                try {
                    return NumberFormat.getInstance(Locale.FRANCE).parse(parameter.getValue()).doubleValue();
                } catch (Exception e) {
                    throw new ParseZiehlAbeggApiResponseFailed();
                }
            }
        }
        return 0;
    }

    private AcousticPerformance getThickness(InstallationToSelection installation) {

        if (installation.getThickness() == 25) {
            return ACOUSTIC_25;
        } else if (installation.getThickness() == 45) {
            return ACOUSTIC_45;
        } else throw new UnselectedThickness();
    }

    private double getGeometricMeanFrequency(AcousticPerformance acousticPerformance) {

        return 10 * log10(
                pow(10, 0.1 * acousticPerformance.getHz63()) +
                        pow(10, 0.1 * acousticPerformance.getHz125()) +
                        pow(10, 0.1 * acousticPerformance.getHz250()) +
                        pow(10, 0.1 * acousticPerformance.getHz500()) +
                        pow(10, 0.1 * acousticPerformance.getHz1000()) +
                        pow(10, 0.1 * acousticPerformance.getHz2000()) +
                        pow(10, 0.1 * acousticPerformance.getHz4000()) +
                        pow(10, 0.1 * acousticPerformance.getHz8000())
        );
    }
}
