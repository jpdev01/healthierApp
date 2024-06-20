package com.udesc.healthier.api.dto;

public class CreateUserRequestDTO {

    String name;
    String email;
    String password;

    public CreateUserRequestDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
