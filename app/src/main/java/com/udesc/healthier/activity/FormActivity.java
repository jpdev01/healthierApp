package com.udesc.healthier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.udesc.healthier.api.ApiService;
import com.udesc.healthier.api.dto.GetUserInfoResponseDTO;
import com.udesc.healthier.R;
import com.udesc.healthier.api.RetrofitClient;
import com.udesc.healthier.api.dto.UserInfoDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {

    private EditText editTextWeight, editTextHeight, editTextDateOfBirth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        fillData();
        buttonSubmit.setOnClickListener(v -> sendFormData());
    }

    private void fillData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GetUserInfoResponseDTO> call = apiService.getInfo();

        call.enqueue(new Callback<GetUserInfoResponseDTO>() {
            @Override
            public void onResponse(Call<GetUserInfoResponseDTO> call, Response<GetUserInfoResponseDTO> response) {
                if (response.isSuccessful()) {
                    GetUserInfoResponseDTO info = response.body();
                    editTextWeight.setText(info.getWeight().toString());
                    editTextHeight.setText(info.getHeight().toString());
                    editTextDateOfBirth.setText(info.getDateOfBirth().toString());
                } else {
                    if (404 == response.code()) return;
                    showFailureAlert();
                }
            }

            @Override
            public void onFailure(Call<GetUserInfoResponseDTO> call, Throwable t) {
                showFailureAlert();
            }
        });
    }

    private void sendFormData() {
        // Obter os valores dos campos
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        double height = Double.parseDouble(editTextHeight.getText().toString());
        String dateOfBirth = String.valueOf(editTextDateOfBirth.getText());

        // Criar JSON
        try {
            UserInfoDTO userInfo = new UserInfoDTO(weight, height, dateOfBirth);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Void> call = apiService.sendForm(userInfo);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showDoneAlert();
                    } else {
                        showFailureAlert();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showFailureAlert();
                }
            });
        } catch (Exception exception) {
            System.out.println("Erro desconhecido");
        }
    }

    private void showDoneAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Seu cadastro foi atualizado, seus novos treinos serão gerados com base nas suas informações atualizadas.")
                .setCancelable(true)
                .setOnCancelListener(v -> {
                    Intent intent = new Intent(FormActivity.this, MainActivity.class);
                    startActivity(intent);
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showFailureAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Não foi possível atualizar o seu cadastro. Tente novamente mais tarde.")
                .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }
}