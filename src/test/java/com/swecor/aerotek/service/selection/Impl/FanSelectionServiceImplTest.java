package com.swecor.aerotek.service.selection.Impl;

import com.swecor.aerotek.model.library.section.ElectricHeater;
import com.swecor.aerotek.model.selection.Instalation.InstallationToSelection;
import com.swecor.aerotek.model.selection.Instalation.flow.SectionToConfigurator;
import com.swecor.aerotek.persist.selction.RoenEstHeatExchangerRepository;
import com.swecor.aerotek.rest.internalApi.roenEst.RoenEstClient;
import com.swecor.aerotek.service.Constants;
import com.swecor.aerotek.service.media.pdf.PdfBuilder;
import com.swecor.aerotek.persist.library.AirConsumptionRepository;
import com.swecor.aerotek.persist.library.ParameterRepository;
import com.swecor.aerotek.persist.library.SectionRepository;
import com.swecor.aerotek.persist.selction.InstallationToSelectionRepository;
import com.swecor.aerotek.rest.externalApi.ziehlAbegg.ZiehlAbeggClient;
import com.swecor.aerotek.service.calculation.AirSpeedService;
import com.swecor.aerotek.service.calculation.FlowPressureLossService;
import com.swecor.aerotek.service.calculation.PressureLossService;
import com.swecor.aerotek.service.calculation.sectionCalc.AirValveCalculationService;
import com.swecor.aerotek.service.selection.FanSelectionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-prod.yaml")
class FanSelectionServiceImplTest {

    @Value("${upload.path}")
    private String propertyPath;

    @Autowired
    private FanSelectionService fanSelectionService;

    @Autowired
    private InstallationToSelectionRepository installationToSelectionRepository;

    @Autowired
    private ZiehlAbeggClient ziehlAbeggClient;

    @Autowired
    private AirConsumptionRepository airConsumptionRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private PressureLossService pressureLossService;

    @Autowired
    private FlowPressureLossService flowPressureLossService;

    @Autowired
    private AirValveCalculationService airValveCalculationService;

    @Autowired
    private AirSpeedService airSpeedService;

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private PdfBuilder pdfBuilder;

    @Autowired
    private RoenEstClient roenEstClient;

    @Autowired
    private RoenEstHeatExchangerRepository roenEstHeatExchangerRepository;

    @Autowired
    private CoolerSelectionServiceImpl heaterExchangerSelectionService;

    @BeforeEach
    public void printTestData() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<Start test>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<---------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<---------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<---------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<---------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<---------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<---------->>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @AfterEach
    public void printEndTest() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<-------->>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<End test>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Test
    void testPdf(){

        ElectricHeater e = sectionRepository.getElectricHeater((byte) 4, 11, Constants.NUMBER_OF_CONTROL_STEPS, "2", Constants.HEAT_POWER, 40);
        System.out.println(e.getId());
        System.out.println(e.getValue());
    }
}