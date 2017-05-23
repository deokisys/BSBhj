package com.bussystemforblind.bsb;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by dlwlg on 2017-05-16.
 */

public class MsgWriter extends Thread {
    String msg = null;
    BufferedWriter output_stream = null;
    boolean end = false;
    public MsgWriter(BufferedWriter output_stream, String msg) {
        this.output_stream = output_stream;
        this.msg = msg;
    }
    public void run() {
        try {
            output_stream.write(msg);
            output_stream.newLine();
            output_stream.flush();
        } catch (IOException e) {
            Log.e("MsgWriter", "run: write 에러", e);
        }
        end = true;
    }
    public void waitComplete() {
        while (!end);
    }
}
