package com.swecor.aerotek.model.library.element;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Map;

@Data
@Builder
public class ElementRequestDTO {

    @NotNull(message = "id не может быть null")
    private int id;

    @NotBlank(message = "Наименование секции не может быть пустым")
    @NotEmpty(message = "Наименование секции не может быть пустым")
    @NotNull(message = "Наименование секции не может быть null")
    @Size(min = 1, max = 100, message
            = "Наименование элемента должено быть от 1 до 100 символов")
    private String name;

    @Size(max = 100, message
            = "Марка элемента должна быть не более 100 символов")
    private String brand;

    @Size(max = 100, message
            = "Код элемента должен быть оне более 100 символов")
    private String code;

    @Size(max = 100, message
            = "Индекс элемента должен быть не более 100 символов")
    private String index;

    @Digits(integer=10, fraction=2)
    private Float costPrice;

    @Size(max = 100, message
            = "Описание элемента должно быть не более 100 символов")
    private String description;

    @Size(max = 100, message
            = "Примечание элемента должно быть не более 100 символов")
    private String note;

    @Size(max = 100, message
            = "Код чертежа элемента должен быть не более 100 символов")
    private String drawingCode;

    @Min(1)
    private Integer elementTypeId;

    private Map<Integer, String> parametersValues;

}
