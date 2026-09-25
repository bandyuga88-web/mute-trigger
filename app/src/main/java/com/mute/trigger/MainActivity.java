package com.mute.trigger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ждем 4 секунды после старта лаунчера, пока adbd полностью поднимется
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Отправляем первое нажатие через ADB сокет
                            sendAdbCommand("shell:input keyevent 164");
                            
                            // Пауза между нажатиями
                            Thread.sleep(600);
                            
                            // Второе нажатие
                            sendAdbCommand("shell:input keyevent 164");

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // Жестко завершаем процесс, чтобы приложение вообще не висело в памяти
                            finishAffinity();
                            System.exit(0);
                        }
                    }
                }).start();
            }
        }, 4000);
    }

    private void sendAdbCommand(String command) {
        Socket socket = null;
        try {
            // Подключаемся к локальному ADB демону на телевизоре
            socket = new Socket("127.0.0.1", 5555);
            OutputStream out = socket.getOutputStream();

            // Формируем стандартный запрос к adbd сервису
            // Демон принимает строку запроса, префикс "host:" говорит о системной команде
            String req = "host:transport-any";
            byte[] reqBytes = (String.format("%04X", req.length()) + req).getBytes(StandardCharsets.UTF_8);
            out.write(reqBytes);
            out.flush();

            // Читаем ответ (можно пропустить для легковерия, дав паузу)
            Thread.sleep(100);

            // Отправляем саму команду оболочки
            byte[] cmdBytes = (String.format("%04X", command.length()) + command).getBytes(StandardCharsets.UTF_8);
            out.write(cmdBytes);
            out.flush();

            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ignored) {}
            }
        }
    }
}
