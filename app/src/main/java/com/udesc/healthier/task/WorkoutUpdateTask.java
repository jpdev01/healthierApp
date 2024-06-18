package com.udesc.healthier.task;

import android.widget.TextView;

import com.udesc.healthier.api.ApiService;
import com.udesc.healthier.api.RetrofitClient;
import com.udesc.healthier.api.dto.GetWorkoutResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutUpdateTask extends BackgroundTask{
    Integer currentVersion;
    Boolean keepFetching = true;
    TextView responseTextView;

    public WorkoutUpdateTask(Integer currentVersion, TextView responseTextView) {
        this.currentVersion = currentVersion;
        this.responseTextView = responseTextView;
    }

    @Override
    public void get() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetWorkoutResponseDTO> call = apiService.getCurrentWorkoutPlan();

        call.enqueue(new Callback<GetWorkoutResponseDTO>() {
            @Override
            public void onResponse(Call<GetWorkoutResponseDTO> call, Response<GetWorkoutResponseDTO> response) {
                boolean updated = !currentVersion.equals(response.body().getVersion());
                if (!updated) return;

                keepFetching = false;
                if (response.isSuccessful()) {
                    responseTextView.setText(response.body().getDescription());
                } else {
                    responseTextView.setText("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GetWorkoutResponseDTO> call, Throwable t) {
                responseTextView.setText("Request failed: " + t.getMessage());
            }
        });
    }
}
