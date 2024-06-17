package com.udesc.healthier;

import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class BackgroundTask {
    private Executor executor = Executors.newSingleThreadExecutor(); // Executor para executar a tarefa em segundo plano
    private static final long INTERVAL_BETWEEN_REQUESTS_MILLIS = 10000;
    private static final int MAX_CALLS = 10;

    private boolean keepFetching = true;

    public void execute(TextView responseTextView, Integer workoutCurrentVersion) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        AtomicInteger callsCount = new AtomicInteger(0);
        executor.execute(() -> {
            while (keepFetching) {
                int call = callsCount.incrementAndGet();
                if (call > MAX_CALLS) {
                    keepFetching = false;
                    return;
                }
                get(apiService, responseTextView, workoutCurrentVersion);
                if (keepFetching) {
                    try {
                        Thread.sleep(INTERVAL_BETWEEN_REQUESTS_MILLIS);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    private void get(ApiService apiService, TextView responseTextView, Integer workoutCurrentVersion) {
        Call<GetWorkoutResponseDTO> call = apiService.getCurrentWorkoutPlan();

        call.enqueue(new Callback<GetWorkoutResponseDTO>() {
            @Override
            public void onResponse(Call<GetWorkoutResponseDTO> call, Response<GetWorkoutResponseDTO> response) {
                boolean updated = !workoutCurrentVersion.equals(response.body().getVersion());
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