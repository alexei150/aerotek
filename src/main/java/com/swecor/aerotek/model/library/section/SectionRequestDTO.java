package com.swecor.aerotek.model.library.section;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Map;

@Data
@Builder
public class SectionRequestDTO {

    @NotNull(message = "id не может быть null")
    private int id;

    @NotBlank(message = "Наименование секции не может быть пустым")
    @NotEmpty(message = "Наименование секции не может быть пустым")
    @NotNull(message = "Наименование секции не может быть null")
    @Size(min = 1, max = 100, message
            = "Наименование секции должено быть от 1 до 100 символов")
    private String name;

    @NotBlank(message = "Код чертежа не может быть пустым")
    @NotEmpty(message = "Код чертежа не может быть пустым")
    @NotNull(message = "Код чертежа не может быть null")
    @Size(max = 100, message
            = "Код чертежа должен быть не более 100 символов")
    private String drawingCode;

    @Size(max = 100, message
            = "Описание элемента должно быть не более 100 символов")
    private String description;

    @Size(max = 100, message
            = "Примечание элемента должно быть не более 100 символов")
    private String note;

    @Digits(integer=10, fraction=2)
    private Float buildCoefficient;

    @Digits(integer=10, fraction=2)
    private Float hardwareCoefficient;

    @Digits(integer=10, fraction=2)
    private Float sectionArea;

    @Min(value = 1, message = "Типоразмер не может быть менее 1")
    @Min(value = 1, message = "Типоразмер не может быть более 24")
    private Byte standardSize;


    @Min(1)
    private Integer sectionTypeId;

    //todo по хорошему коечно надо сделать более подробную валидацию Map, но пока не понятно какая бизнесс логика
    private Map<Integer, String> parametersValues;

    private Map<Integer, Integer> elementsCount;

    private AcousticPerformance acousticPerformance;

    private Map<Integer, @Digits(integer=10, fraction=2)Float> airConsumption;

    private Map<Integer, String> variableElements;
}
