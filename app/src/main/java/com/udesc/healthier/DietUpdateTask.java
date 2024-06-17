package com.udesc.healthier;

import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietUpdateTask extends BackgroundTask {

    Integer currentVersion;
    Boolean keepFetching = true;
    TextView responseTextView;

    public DietUpdateTask(Integer currentVersion, TextView responseTextView) {
        this.currentVersion = currentVersion;
        this.responseTextView = responseTextView;
    }

    @Override
    public void get() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetDietResponseDTO> call = apiService.getDiet();

        call.enqueue(new Callback<GetDietResponseDTO>() {
            @Override
            public void onResponse(Call<GetDietResponseDTO> call, Response<GetDietResponseDTO> response) {
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
            public void onFailure(Call<GetDietResponseDTO> call, Throwable t) {
                responseTextView.setText("Request failed: " + t.getMessage());
            }
        });
    }
}
