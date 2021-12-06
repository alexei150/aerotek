package com.swecor.aerotek.rest.security;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
