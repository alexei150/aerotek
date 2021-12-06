package com.swecor.aerotek.model.library.sectionType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class SectionTypeRequestDTO {

    @NotNull(message = "id не может быть null")
    private int id;

    @NotBlank(message = "Наименование типа секции не может быть пустым")
    @NotEmpty(message = "Наименование типа секции не может быть пустым")
    @NotNull(message = "Наименование типа секции не может быть null")
    @Size(max = 100, message
            = "Наименование типа секции должено быть не более 100 символов")
    private String name;

    @NotBlank(message = "Код секции не может быть пустым")
    @NotEmpty(message = "Код секции не может быть пустым")
    @NotNull(message = "Код секции не может быть null")
    @Size(min = 1, max = 3, message
            = "Колличество символов в коде типа секции должено быть от 1 до 3 символов")
    private String code;

    @Size(max = 100, message
            = "Описание типа секции должено быть не более 100 символов")
    private String note;

    private List<Integer> parameters;

    @NotNull(message = "Изображение типа секции не может быть null")
    private Integer image;
    @NotNull(message = "Картинка базового положения не может быть null")
    private Integer iconBase;
    @NotNull(message = "Картинка положения притока вправо не может быть null")
    private Integer iconInflowToRight;
    @NotNull(message = "Картинка положения притока влево не может быть null")
    private Integer iconInflowToLeft;
    @NotNull(message = "Картинка положения вытяжки вправо не может быть null")
    private Integer iconOutflowToRight;
    @NotNull(message = "Картинка положения вытяжки влево не может быть null")
    private Integer iconOutflowToLeft;

}
