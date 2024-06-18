package com.udesc.healthier;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        validateAlreadyLogged();
        loginButton.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
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
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
}