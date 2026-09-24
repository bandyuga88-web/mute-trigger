package com.mute.trigger;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Даем системе 4 секунды на полную загрузку лаунчера и сети
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                executeAdbMuteSequence();
            }
        }, 4000);
    }

    private void executeAdbMuteSequence() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Способ А: Подключаемся через localhost (127.0.0.1), так как adbd слушает внутри системы
                    Process pConnect = Runtime.getRuntime().exec("adb connect 127.0.0.1:5555");
                    pConnect.waitFor();

                    // Первое нажатие MUTE через сетевой adb shell
                    Process pMute1 = Runtime.getRuntime().exec("adb shell input keyevent 164");
                    pMute1.waitFor();

                    // Пауза полсекунды между нажатиями
                    Thread.sleep(500);

                    // Второе нажатие MUTE
                    Process pMute2 = Runtime.getRuntime().exec("adb shell input keyevent 164");
                    pMute2.waitFor();

                    // Закрываем приложение
                    finishAffinity();
                    System.exit(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
