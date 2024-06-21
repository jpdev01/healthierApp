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
import com.udesc.healthier.task.DietUpdateTask;
import com.udesc.healthier.api.dto.GetDietResponseDTO;
import com.udesc.healthier.R;
import com.udesc.healthier.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietActivity extends AppCompatActivity {

    private TextView responseTextView;

    private Button updateButton;
    private Long dietCurrentVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.diet), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        checkInternet();

        // Vincula o elemento da UI ao objeto Java
        responseTextView = findViewById(R.id.responseTextView);

        getCurrentDiet();
        updateButton = findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(v -> {
            requestUpdate();
            showUpdatingAlert();
        });
    }

    private void showUpdatingAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Estamos atualizando sua dieta. Isso pode levar alguns segundos.")
                .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void checkInternet() {
        String permission = Manifest.permission.INTERNET;
        int granted = PackageManager.PERMISSION_GRANTED;
        if (ContextCompat.checkSelfPermission(this, permission) != granted) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
    }

    private void getCurrentDiet() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetDietResponseDTO> call = apiService.getDiet();

        call.enqueue(new Callback<GetDietResponseDTO>() {
            @Override
            public void onResponse(Call<GetDietResponseDTO> call, Response<GetDietResponseDTO> response) {
                if (response.isSuccessful()) {
                    dietCurrentVersion = response.body().getId();
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

    private void requestUpdate() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.requestDietUpdate();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    new DietUpdateTask(dietCurrentVersion, responseTextView).execute();
                } else {
                    if (response.code() == 404) {
                        responseTextView.setText("Você ainda não tem uma dieta. Solicite-a abaixo");
                    } else {
                        responseTextView.setText("Não foi possível carregar sua dieta. Tente novamente mais tarde.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                responseTextView.setText("Não foi possível carregar sua dieta. Tente novamente mais tarde.");
            }
        });
    }
}
