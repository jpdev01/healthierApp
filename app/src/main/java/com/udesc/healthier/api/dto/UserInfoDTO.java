package com.udesc.healthier.api.dto;

public class UserInfoDTO {

    double weight;
    double height;
    String dateOfBirth;

    public UserInfoDTO(double weight, double height, String dateOfBirth) {
        this.weight = weight;
        this.height = height;
        this.dateOfBirth = dateOfBirth;
    }
}
