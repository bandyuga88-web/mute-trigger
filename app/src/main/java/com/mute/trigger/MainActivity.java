package com.example.mutelauncher; // Замените на пакет вашего приложения

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class MainActivity extends Activity {

    private static final String TAG = "MuteApp";
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Первое нажатие MUTE происходит сразу при запуске
        sendMuteKeyEvent();

        // 2. Второе нажатие MUTE через 1 секунду (1000 мс)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMuteKeyEvent();
            }
        }, 1000);

        // 3. Выгрузка/закрытие приложения через 5 секунд (5000 мс)
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Закрытие приложения...");
                finishAffinity(); // Закрывает активность
                System.exit(0);   // Завершает системный процесс
            }
        }, 5000);
    }

    // Метод, который программно «нажимает» кнопку MUTE (код 164)
    private void sendMuteKeyEvent() {
        try {
            // Выполняем системную команду эмуляции нажатия клавиши
            Process process = Runtime.getRuntime().exec("input keyevent 164");
            process.waitFor();
            Log.d(TAG, "Команда MUTE (164) успешно отправлена");
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при отправке keyevent", e);
        }
    }
}
