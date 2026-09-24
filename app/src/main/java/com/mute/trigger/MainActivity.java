package com.mute.trigger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executeMute();

        // Пауза 1 секунда для второго нажатия и закрытие
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                executeMute();
                finish();
                System.exit(0);
            }
        }, 1000);
    }

    private void executeMute() {
        try {
            Runtime.getRuntime().exec("input keyevent 164");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
