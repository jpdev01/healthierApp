package com.udesc.healthier;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView responseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        checkContactsReadPermission();

        // Vincula o elemento da UI ao objeto Java
        responseTextView = findViewById(R.id.responseTextView);

        // Faz a requisição à API
        makeApiRequest();
    }

    protected void checkContactsReadPermission() {
        String permission = Manifest.permission.INTERNET;
        int granted = PackageManager.PERMISSION_GRANTED;
        if (ContextCompat.checkSelfPermission(this, permission) != granted) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
    }

    private void makeApiRequest() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetWorkoutResponseDTO> call = apiService.getTeste();

        call.enqueue(new Callback<GetWorkoutResponseDTO>() {
            @Override
            public void onResponse(Call<GetWorkoutResponseDTO> call, Response<GetWorkoutResponseDTO> response) {
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