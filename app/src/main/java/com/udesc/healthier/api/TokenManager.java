package com.udesc.healthier.api;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String SHARED_PREFS_NAME = "my_shared_prefs";
    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_STATUS = "user_status";

    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_JWT_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    public String getStatus() {
        return sharedPreferences.getString(KEY_STATUS, "ACTIVE");
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_JWT_TOKEN);
        editor.apply();
    }
}