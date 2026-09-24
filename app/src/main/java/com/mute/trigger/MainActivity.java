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
        sendMuteKey();

        // Пауза 1 секунда (1000 мс) и второе нажатие
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                sendMuteKey();
                
                // Закрываем приложение и выгружаем из памяти
                finish();
                System.exit(0);
            }
        }, 1000);
    }

    private void sendMuteKey() {
        try {
            Runtime.getRuntime().exec(new String[]{"input", "keyevent", "164"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
