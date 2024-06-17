package com.udesc.healthier;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    @GET("/workout-plan")
    Call<GetWorkoutResponseDTO> getCurrentWorkoutPlan();

    @GET("/diet")
    Call<GetDietResponseDTO> getDiet();

    @PUT("workout-plan")
    Call<Void> requestWorkoutPlanUpdate();

    @PUT("diet")
    Call<Void> requestDietUpdate();


    @PUT("user-info")
    Call<Void> sendForm(@Body UserInfoDTO userInfoDTO);

    @GET("user-info")
    Call<GetUserInfoResponseDTO> getInfo();
}