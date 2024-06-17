package com.udesc.healthier;

import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract class BackgroundTask {
    private Executor executor = Executors.newSingleThreadExecutor(); // Executor para executar a tarefa em segundo plano
    private static final long INTERVAL_BETWEEN_REQUESTS_MILLIS = 10000;
    private static final int MAX_CALLS = 10;

    private boolean keepFetching = true;

    public void execute() {
        try {
            AtomicInteger callsCount = new AtomicInteger(0);
            executor.execute(() -> {
                while (keepFetching) {
                    int call = callsCount.incrementAndGet();
                    if (call > MAX_CALLS) {
                        keepFetching = false;
                        return;
                    }
                    get();
                    if (keepFetching) {
                        try {
                            Thread.sleep(INTERVAL_BETWEEN_REQUESTS_MILLIS);
                        } catch (InterruptedException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
        } catch (Exception exception) {
            System.out.println("Ocorreu um erro desconhecido ao rodar atualização em segundo plano");
        }
    }

    public abstract void get();

}