package com.bussystemforblind.bsb;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Created by dlwlg on 2017-05-16.
 */

public class SocketManager {
    public final static String SERVER_IP = "117.20.90.63";
    public final static int SERVER_PORT = 9446;
    private static SocketManager manager;
    private SocketGenerator sg = null;
    private BufferedWriter output_stream = null; // 출력 버퍼
    private BufferedReader input_stream = null; // 입력 버퍼

    private SocketManager() {
        sg = SocketGenerator.getGenerator();
        sg.start();
        sg.waitComplete();
        output_stream = sg.getWriter();
        input_stream = sg.getReader();
    }

    public static SocketManager getManager() {
        if (manager == null)
            manager = new SocketManager();
        return manager;
    }
    public void closeSocket() {
    }
    public void sendMsg(String msg) {
        MsgWriter mw = new MsgWriter(output_stream, msg);
        mw.start();
        mw.waitComplete();
    }
    public String getMsg() {
        MsgReceiver mr = new MsgReceiver(input_stream);
        mr.start();
        mr.waitComplete();
        String msg = mr.getMsg();
        return msg;
    }
}
