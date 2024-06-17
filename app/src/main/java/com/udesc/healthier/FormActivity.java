package com.udesc.healthier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends AppCompatActivity {

    private EditText editTextWeight, editTextHeight, editTextAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        Button buttonSubmit = findViewById(R.id.buttonSubmit);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextAge = findViewById(R.id.editTextAge);
        buttonSubmit.setOnClickListener(v -> sendFormData());
    }

    private void sendFormData() {
        // Obter os valores dos campos
        double weight = Double.parseDouble(editTextWeight.getText().toString());
        double height = Double.parseDouble(editTextHeight.getText().toString());
        Integer age = Integer.valueOf(editTextAge.getText().toString());

        // Criar JSON
        try {
            UserInfoDTO userInfo = new UserInfoDTO(weight, height, age);

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