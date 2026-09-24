package com.mute.trigger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Первое нажатие MUTE
        sendMute();

        // Пауза 1 секунда и второе нажатие MUTE, затем выход
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMute();
                
                // Закрываем приложение
                finish();
                System.exit(0);
            }
        }, 1000);
    }

    private void sendMute() {
        try {
            // Пробуем вызвать системный бинарник input по точному пути
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "/system/bin/input keyevent 164"});
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Запасной вариант через стандартную оболочку
                Runtime.getRuntime().exec("input keyevent 164");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
