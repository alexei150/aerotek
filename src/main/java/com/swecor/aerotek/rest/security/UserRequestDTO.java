package com.swecor.aerotek.rest.security;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserRequestDTO {

    @Email
    private String email;

    @NotNull(message = "Пароль не может быть null")
    @Size(min = 8, max = 100, message
            = "Пароль должен быть от 8 символов")
    private String password;

    @NotNull(message = "Имя не может быть null")
    @Size(min = 1, max = 50, message
            = "Имя должно быть от 1 символа")
    private String firstName;

    @NotNull(message = "Фамилия не может быть null")
    @Size(min = 1, max = 50, message
            = "Фамилия должна быть от 1 символа")
    private String lastName;

    private String patronymic;

    private String company;

    private String phoneNumber;

    //private Integer supervisorId;
}
