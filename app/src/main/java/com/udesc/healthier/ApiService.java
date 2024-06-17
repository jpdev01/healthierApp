package com.udesc.healthier;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {

    @GET("/workout-plan")
    Call<GetWorkoutResponseDTO> getCurrentWorkoutPlan();

    @PUT("workout-plan")
    Call<Void> requestWorkoutPlanUpdate();
}