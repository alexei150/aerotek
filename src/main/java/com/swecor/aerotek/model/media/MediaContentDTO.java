package com.swecor.aerotek.model.media;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class MediaContentDTO {

    @NotNull(message = "id не может быть null")
    private Integer id;

    @NotBlank(message = "Наименование картинки типа секции не может быть пустым")
    @NotEmpty(message = "Наименование картинки типа секции не может быть пустым")
    @NotNull(message = "Наименование картинки типа секции не может быть null")
    @Size(min = 1, max = 100, message
            = "Наименование картинки типа секции должено быть от 1 до 100 символов")
    private String name;

    @Size(max = 100, message
            = "Описание картинки типа секции должно быть не более 100 символов")
    private String description;
}
