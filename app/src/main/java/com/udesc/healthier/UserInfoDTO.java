package com.udesc.healthier;

public class UserInfoDTO {

    double weight;
    double height;

    Integer age;

    public UserInfoDTO(double weight, double height, Integer age) {
        this.weight = weight;
        this.height = height;
        this.age = age;
    }
}
