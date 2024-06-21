package com.udesc.healthier.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.udesc.healthier.api.ApiService;
import com.udesc.healthier.api.dto.GetWorkoutResponseDTO;
import com.udesc.healthier.R;
import com.udesc.healthier.api.RetrofitClient;
import com.udesc.healthier.task.WorkoutUpdateTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkoutActivity  extends AppCompatActivity {

    private TextView responseTextView;

    private Button updateButton;
    private Long workoutCurrentVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.workout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        checkContactsReadPermission();

        // Vincula o elemento da UI ao objeto Java
        responseTextView = findViewById(R.id.responseTextView);

        updateButton = findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(v -> {
            requestWorkoutUpdate();
            showUpdatingAlert();
            new WorkoutUpdateTask(workoutCurrentVersion, responseTextView).execute();
        });

        // Faz a requisição à API
        getCurrentWorkout();
    }

    private void showUpdatingAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Estamos atualizando seu treino. Isso pode levar alguns segundos.")
                .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void checkContactsReadPermission() {
        String permission = Manifest.permission.INTERNET;
        int granted = PackageManager.PERMISSION_GRANTED;
        if (ContextCompat.checkSelfPermission(this, permission) != granted) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
    }

    private void getCurrentWorkout() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetWorkoutResponseDTO> call = apiService.getCurrentWorkoutPlan();

        call.enqueue(new Callback<GetWorkoutResponseDTO>() {
            @Override
            public void onResponse(Call<GetWorkoutResponseDTO> call, Response<GetWorkoutResponseDTO> response) {
                if (response.isSuccessful()) {
                    workoutCurrentVersion = response.body().getId();
                    responseTextView.setText(response.body().getDescription());
                } else {
                    if (response.code() == 404) {
                        responseTextView.setText("Você ainda não tem um treino. Solicite-o abaixo");
                    } else {
                        responseTextView.setText("Não foi possível carregar seu treino. Tente novamente mais tarde.");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetWorkoutResponseDTO> call, Throwable t) {
                responseTextView.setText("Não foi possível carregar seu treino. Tente novamente mais tarde.");
            }
        });
    }

    private void requestWorkoutUpdate() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.requestWorkoutPlanUpdate();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
//                    responseTextView.setText(response.body().getDescription());
                } else {
                    responseTextView.setText("Request failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                responseTextView.setText("Request failed: " + t.getMessage());
            }
        });
    }
}
