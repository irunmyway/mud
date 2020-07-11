package com.eztv.mud.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ez.socket.SocketClient;
import com.ez.utils.BDebug;

public class SocketService extends Service
{
    public IBinder onBind(Intent paramIntent)
    {
        throw new UnsupportedOperationException("xxx");
    }

    public void onCreate()
    {
        super.onCreate();
        SocketClient.getInstance();
        BDebug.trace("创建套接字完成。");
    }
}
