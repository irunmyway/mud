package com.ez.socket;


import com.ez.socket.callback.HeartbeatCallback;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HeartbeatClient extends Thread {
    long checkDelay = 500;//检测心跳间隔的间隔
    long keepAliveDelay = 5000;//心跳间隔2s
    private Socket socket = null;//定义客户
    private DataOutputStream out = null;
    private HeartbeatCallback heartbeatCallback;
    private long lastSendTime = System.currentTimeMillis(); //最后一次发送数据的时间
    private boolean isExit;//心跳结束标记

    //创建心跳
    public HeartbeatClient createHeatbeat(Socket socket, HeartbeatCallback heartbeatCallback) {
        this.socket = socket;
        this.heartbeatCallback = heartbeatCallback;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    //设置退出
    public void setExit(boolean exit) {
        isExit = exit;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (!isExit) {
                if (System.currentTimeMillis() - lastSendTime > keepAliveDelay) {
                    lastSendTime = System.currentTimeMillis();
                    //预发送心跳数据
                    heartbeatCallback.onPreSend(out);
                } else {
                    try {
                        Thread.sleep(checkDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            heartbeatCallback.onStopHeartbeat();
        } catch (Exception e) {
        }
    }

}