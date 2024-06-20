package com.udesc.healthier.api;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.0.2:8080";
    private static Retrofit retrofit = null;
    private static String token;

    public static void init(Context context) {
        String authToken = new TokenManager(context).getToken();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(authToken))
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient) // Adicione o cliente HTTP com o interceptor
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void destroy() {
        retrofit = null;
    }
}
