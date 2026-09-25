package com.mute.trigger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends Activity {

    private boolean isExecuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected voidonResume() {
        super.onResume();

        // Защита от повторного срабатывания при перерисовке активности
        if (isExecuted) return;
        isExecuted = true;

        // Ждем 12 секунд после полного открытия экрана, чтобы система освободилась
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 1. Пробуем прямой вызов аудиослужбы через Binder
                            Runtime.getRuntime().exec("service call audio 3 i32 3 i32 0 i32 1");
                            Thread.sleep(600);
                            Runtime.getRuntime().exec("service call audio 3 i32 3 i32 0 i32 1");

                            // 2. Страховка: классический input keyevent, когда система свободна
                            Thread.sleep(800);
                            Runtime.getRuntime().exec("input keyevent 164");
                            Thread.sleep(500);
                            Runtime.getRuntime().exec("input keyevent 164");

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // Безопасно закрываем приложение после выполнения задач
                            finishAndRemoveTask();
                            System.exit(0);
                        }
                    }
                }).start();
            }
        }, 12000);
    }
}
