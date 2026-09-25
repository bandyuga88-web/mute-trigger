package com.mute.trigger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Даем системе 6 секунд на полную загрузку после холодного старта
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    // Эмулируем нажатие кнопки MUTE (164) через диспетчеризацию окна
                    getWindow().getDecorView().dispatchKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_DOWN, 164)
                    );
                    getWindow().getDecorView().dispatchKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_UP, 164)
                    );

                    Thread.sleep(500);

                    // Второе нажатие
                    getWindow().getDecorView().dispatchKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_DOWN, 164)
                    );
                    getWindow().getDecorView().dispatchKeyEvent(
                        new KeyEvent(KeyEvent.ACTION_UP, 164)
                    );

                } catch (Exception e) {
                    // Запасной вариант через Runtime, если окно еще не получило фокус
                    try {
                        Runtime.getRuntime().exec("input keyevent 164");
                        Thread.sleep(500);
                        Runtime.getRuntime().exec("input keyevent 164");
                    } catch (Exception ignored) {}
                } finally {
                    // Мгновенно выгружаем приложение из памяти
                    finishAffinity();
                    System.exit(0);
                }
            }
        }, 6000);
    }
}
