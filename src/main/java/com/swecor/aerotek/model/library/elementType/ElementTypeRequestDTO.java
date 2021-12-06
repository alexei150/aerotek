package com.swecor.aerotek.model.library.elementType;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ElementTypeRequestDTO {


    @NotNull(message = "id не может быть null")
    private int id;

    @NotBlank(message = "Наименование типа элемента не может быть пустым")
    @NotEmpty(message = "Наименование типа элемента не может быть пустым")
    @NotNull(message = "Наименование типа элемента не может быть null")
    @Size(min = 1, max = 100, message
            = "Наименование типа элемента должено быть от 1 до 100 символов")
    private String name;

    @Size(max = 100, message
            = "Описание типа элемента должено быть не более 100 символов")
    private String note;

    private List<Integer> parameters;
}
