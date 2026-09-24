package com.mute.trigger;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ждем 5 секунд до полной загрузки системы и демона adbd
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Отправляем двойной мьют через настоящий ADB-протокол по сокету
                        sendAdbCommand("input keyevent 164");
                        try { Thread.sleep(600); } catch (Exception ignored) {}
                        sendAdbCommand("input keyevent 164");

                        // Выходим
                        finishAffinity();
                        System.exit(0);
                    }
                }).start();
            }
        }, 5000);
    }

    private void sendAdbCommand(String cmd) {
        Socket socket = null;
        try {
            // Подключаемся к локальному ADB-серверу на телевизоре
            socket = new Socket("127.0.0.1", 5555);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // 1. Формируем ADB Connect пакет (A_CNXN)
            // Заголовок ADB: A_CNXN (0x4e584e43), arg0 = 0x01000000 (version), arg1 = 1048576 (maxdata)
            byte[] banner = "device::ro.product.name=mstar\0".getBytes(StandardCharsets.UTF_8);
            byte[] header = createAdbHeader(0x4e584e43, 0x01000000, 1048576, banner.length);
            
            out.write(header);
            out.write(banner);
            out.flush();

            // Читаем ответ подключения (пускай пропущено детальное чтение, главное — отправить команду service)
            // 2. Открываем ADB Shell сервис: "shell,..." или "shell:" + команда
            String serviceStr = "shell:" + cmd + "\0";
            byte[] serviceBytes = serviceStr.getBytes(StandardCharsets.UTF_8);
            byte[] openHeader = createAdbHeader(0x4e45504f, 1, 0, serviceBytes.length); // A_OPEN

            out.write(openHeader);
            out.write(serviceBytes);
            out.flush();

            Thread.sleep(300);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try { socket.close(); } catch (Exception ignored) {}
            }
        }
    }

    // Вспомогательный метод для формирования заголовков пакетов ADB протокола
    private byte[] createAdbHeader(int command, int arg0, int arg1, int dataLength) {
        byte[] header = new byte[24];
        // command
        header[0] = (byte) (command & 0xFF);
        header[1] = (byte) ((command >> 8) & 0xFF);
        header[2] = (byte) ((command >> 16) & 0xFF);
        header[3] = (byte) ((command >> 24) & 0xFF);
        // arg0
        header[4] = (byte) (arg0 & 0xFF);
        header[5] = (byte) ((arg0 >> 8) & 0xFF);
        header[6] = (byte) ((arg0 >> 16) & 0xFF);
        header[7] = (byte) ((arg0 >> 24) & 0xFF);
        // arg1
        header[8] = (byte) (arg1 & 0xFF);
        header[9] = (byte) ((arg1 >> 8) & 0xFF);
        header[10] = (byte) ((arg1 >> 16) & 0xFF);
        header[11] = (byte) ((arg1 >> 24) & 0xFF);
        // data length
        header[12] = (byte) (dataLength & 0xFF);
        header[13] = (byte) ((dataLength >> 8) & 0xFF);
        header[14] = (byte) ((dataLength >> 16) & 0xFF);
        header[15] = (byte) ((dataLength >> 24) & 0xFF);
        // data checksum (простой расчет или 0)
        int checksum = 0;
        header[16] = (byte) (checksum & 0xFF);
        header[17] = (byte) ((checksum >> 8) & 0xFF);
        header[18] = (byte) ((checksum >> 16) & 0xFF);
        header[19] = (byte) ((checksum >> 24) & 0xFF);
        // magic (command ^ 0xFFFFFFFF)
        int magic = ~command;
        header[20] = (byte) (magic & 0xFF);
        header[21] = (byte) ((magic >> 8) & 0xFF);
        header[22] = (byte) ((magic >> 16) & 0xFF);
        header[23] = (byte) ((magic >> 24) & 0xFF);

        return header;
    }
}
