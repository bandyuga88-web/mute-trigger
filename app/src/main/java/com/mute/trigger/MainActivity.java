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
                    // Способ 1: Прямая инъекция keyevent через диспетчеризацию активности (Instrumentation)
                    // Это работает внутри приложений без ADB, если окно имеет фокус или через dispatch
                    dispatchMuteKey();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Мгновенно выгружаем приложение из памяти
                    finishAffinity();
                    System.exit(0);
                }
            }
        }, 6000);
    }

    private void dispatchMuteKey() {
        try {
            // Эмулируем нажатие и отпускание кнопки MUTE (код 164)
            // Первое нажатие
            Instrumentation inst = new Instrumentation();
            // Так как Instrumentation требует потока, сделаем это через dispatch
            getWindow().getDecorView().dispatchKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MUTE)
            );
            getWindow().getDecorView().dispatchKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MUTE)
            );

            Thread.sleep(400);

            // Второе нажатие
            getWindow().getDecorView().dispatchKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MUTE)
            );
            getWindow().getDecorView().dispatchKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MUTE)
            );
        } catch (Exception e) {
            // Запасной вариант через стандартный Runtime, если Instrumentation недоступен в контексте Activity
            try {
                Runtime.getRuntime().exec("input keyevent 164");
                Thread.sleep(400);
                Runtime.getRuntime().exec("input keyevent 164");
            } catch (Exception ignored) {}
        }
    }
}
