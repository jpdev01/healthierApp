package com.udesc.healthier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.udesc.healthier.api.ApiService;
import com.udesc.healthier.api.dto.LoginRequestDTO;
import com.udesc.healthier.api.dto.LoginResponseDTO;
import com.udesc.healthier.R;
import com.udesc.healthier.api.RetrofitClient;
import com.udesc.healthier.api.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton, registryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registryButton = findViewById(R.id.registryButton);

        validateAlreadyLogged();
        loginButton.setOnClickListener(v -> performLogin());
        registryButton.setOnClickListener(v -> redirectToRegistration());
    }

    private void performLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<LoginResponseDTO> call = apiService.login(new LoginRequestDTO(email, password));

        call.enqueue(new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenManager tokenManager = new TokenManager(LoginActivity.this);
                    tokenManager.saveToken(response.body().getToken());
                    RetrofitClient.init(LoginActivity.this);
                    Toast.makeText(LoginActivity.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();
                    redirectToMain();
                } else {
                    Toast.makeText(LoginActivity.this, "Não foi possível efetuar seu Login. Tente novamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {
                // Erro de rede
                Toast.makeText(LoginActivity.this, "Erro de rede", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAlreadyLogged() {
        TokenManager tokenManager = new TokenManager(LoginActivity.this);
        if (tokenManager.getToken() != null) redirectToMain();
    }

    private void redirectToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void redirectToRegistration() {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}