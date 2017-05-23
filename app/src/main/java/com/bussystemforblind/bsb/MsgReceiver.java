package com.bussystemforblind.bsb;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by dlwlg on 2017-05-16.
 */

public class MsgReceiver extends Thread {
    String msg = null;
    BufferedReader input_stream = null;
    boolean end = false;
    public MsgReceiver(BufferedReader input_stream) {
        this.input_stream = input_stream;
    }
    public void run() {
        try {
            msg = input_stream.readLine();
        } catch (IOException e) {
            Log.e("MsgReceiver", "run: readLine 에러", e);
        }
        end = true;
    }
    public String getMsg() {
        while (msg == null);
        return msg;
    }
    public void waitComplete() {
        while (!end);
    }
}
