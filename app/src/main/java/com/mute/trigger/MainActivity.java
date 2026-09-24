package com.mute.trigger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Отправляем первый мьют
        sendMuteKey();

        // Ждем 1 секунду и отправляем второй мьют
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMuteKey();
                
                // Закрываем приложение
                finish();
                System.exit(0);
            }
        }, 1000);
    }

    private void sendMuteKey() {
        try {
            // Выполняем ту же команду, что и через ADB
            Runtime.getRuntime().exec(new String[]{"sh", "-c", "input keyevent 164"});
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Запасной вариант вызова
                Runtime.getRuntime().exec("input keyevent 164");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
