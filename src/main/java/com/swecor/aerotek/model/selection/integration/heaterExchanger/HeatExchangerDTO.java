package com.swecor.aerotek.model.selection.integration.heaterExchanger;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
@Builder
public class HeatExchangerDTO {


    @NotNull(message = "id не может быть null")
    private int id;

    @Range(min = 1, max = 4, message
            = "Допустимые моды: 1, 2, 3, 4")
    private byte mode;
    @NotBlank(message = "Индекс теплообменника не может быть пустым")
    @NotEmpty(message = "Индекс теплообменника не может быть пустым")
    @NotNull(message = "Индекс теплообменника не может быть null")
    private String code;
    @Range(min = 1, max = 100, message
            = "Допустимая геометрия : 1-100")
    private int geometry;
    @Range(min = 1, max = 10000, message
            = "Допустимая длина : 1-10000")
    private int length;
    @Range(min = 1, max = 10000, message
            = "Допустимая высота : 1-10000")
    private int height;
    @Range(min = 1, max = 100, message
            = "Допустимое numRows : 1-100")
    private int numRows;
    @Range(min = 1, max = 100, message
            = "Допустимое tubesType : 1-100")
    private int tubesType;
    @Range(min = 1, max = 100, message
            = "Допустимое finSpacing : 1-100")
    private int finSpacing;
    @Range(min = 1, max = 100, message
            = "Допустимое finType : 1-100")
    private int finType;
    @Range(min = 1, max = 100, message
            = "Допустимое circuitsType : 1-100")
    private int circuitsType;
    @Range(min = 1, max = 100, message
            = "Допустимое numCircuits : 1-100")
    private int numCircuits;
    @Range(min = 0, max = 100, message
            = "Допустимое headerConfiguration : 1-100")
    private int headerConfiguration;
    @Range(min = 1, max = 100, message
            = "Допустимое headerValue1 : 1-100")
    private int headerValue1;
    @Range(min = 1, max = 100, message
            = "Допустимое headerValue2 : 1-100")
    private int headerValue2;
}
