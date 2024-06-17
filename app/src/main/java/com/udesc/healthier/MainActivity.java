package com.udesc.healthier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private Button buttonViewWorkout;
    private Button buttonViewDiet;
    private Button buttonManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
}