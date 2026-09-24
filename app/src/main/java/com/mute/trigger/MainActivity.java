package com.mute.trigger;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Переключаем звук первый раз через системный AudioManager
        toggleMute();

        // Пауза 1 секунда и повторное переключение (для двойного нажатия, если требуется)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleMute();
                
                // Закрываем приложение
                finish();
                System.exit(0);
            }
        }, 1000);
    }

    private void toggleMute() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                // Метод для выключения/включения звука на Android TV
                audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_TOGGLE_MUTE,
                    AudioManager.FLAG_SHOW_UI
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
