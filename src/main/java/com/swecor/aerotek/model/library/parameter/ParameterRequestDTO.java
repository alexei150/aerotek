package com.swecor.aerotek.model.library.parameter;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@Builder
public class ParameterRequestDTO {


    @NotNull(message = "id не может быть null")
    private int id;


    @NotBlank(message = "Параметр не может быть пумтым")
    @NotEmpty(message = "Параметр не может быть пумтым")
    @NotNull(message = "Параметр не может быть null")
    @Size(max = 100, message
            = "Наименование параметра должено быть не более 100 символов")
    private String name;

    @Size(max = 50, message
            = "Минимальное значение параметра должено быть не более 50 символов")
    private String minValue;

    @Size(max = 50, message
            = "Максимальное значение параметра должено быть не более 50 символов")
    private String maxValue;

    @Size(max = 50, message
            = "Максимальное значение параметра должено быть не более 50 символов")
    private String unit;

}
