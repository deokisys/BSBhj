package com.bussystemforblind.bsb;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by dlwlg on 2017-05-16.
 */

public class SocketGenerator extends Thread{
    private final String SERVER_IP = "117.20.90.63";
    private final int SERVER_PORT = 9446;
    private Socket socket = null;
    private BufferedWriter output_stream = null; // 출력 버퍼
    private BufferedReader input_stream = null; // 입력 버퍼
    boolean end = false;
    private static SocketGenerator sg = null;

    private SocketGenerator() {}
    public static SocketGenerator getGenerator() {
        if (sg == null)
            sg = new SocketGenerator();
        return sg;
    }

    public void run() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);

            output_stream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            input_stream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            Log.e("ConnectToServer", "doInBackground: 소켓 생성 에러", e);
        }
        end = true;
    }
    public BufferedWriter getWriter() {
        return output_stream;
    }
    public BufferedReader getReader() {
        return input_stream;
    }
    public void waitComplete() {
        while (!end);
    }
}

