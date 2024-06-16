package com.udesc.healthier;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("/workout-plan")
    Call<GetWorkoutResponseDTO> getTeste();
}