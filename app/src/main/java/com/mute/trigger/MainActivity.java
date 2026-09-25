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

        // Ждем 5 секунд, пока система полностью прогрузится после холодного старта
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    
                    if (audioManager != null) {
                        // Пробуем программно переключить состояние мьюта дважды с паузой
                        // STREAM_MUSIC или STREAM_SYSTEM в зависимости от того, куда идет звук
                        audioManager.adjustStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                AudioManager.ADJUST_TOGGLE_MUTE,
                                AudioManager.FLAG_SHOW_UI
                        );

                        Thread.sleep(500);

                        audioManager.adjustStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                AudioManager.ADJUST_TOGGLE_MUTE,
                                AudioManager.FLAG_SHOW_UI
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Мгновенно выгружаем приложение из памяти
                    finishAffinity();
                    System.exit(0);
                }
            }
        }, 5000);
    }
}
