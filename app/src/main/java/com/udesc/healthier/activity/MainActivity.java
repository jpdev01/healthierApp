package com.udesc.healthier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.udesc.healthier.R;
import com.udesc.healthier.api.RetrofitClient;
import com.udesc.healthier.api.TokenManager;

public class MainActivity extends AppCompatActivity {

    private Button buttonViewWorkout, buttonViewDiet, buttonManagement, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RetrofitClient.init(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonViewWorkout = findViewById(R.id.buttonViewWorkout);

        buttonViewWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
            startActivity(intent);
        });

        buttonManagement = findViewById(R.id.buttonManagement);
        buttonManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            startActivity(intent);
        });


        buttonViewDiet = findViewById(R.id.buttonViewDiet);
        buttonViewDiet.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DietActivity.class);
            startActivity(intent);
        });

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        new TokenManager(MainActivity.this).clearToken();
        RetrofitClient.destroy();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}