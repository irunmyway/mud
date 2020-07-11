package com.ez.socket.callback;

import java.net.Socket;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-06-26 9:44
 * 功能: 套接字基础监听
 **/
public interface SocketCallback {
    //出错
    void onError(Exception e);

    //关闭
    void onClosed(Socket socket);

    //已连接
    void onConnected(Socket socket);

    //连接失败
    void onConnectFail(Socket socket, boolean needReconnect);

    //接收
    void onReceive(Socket socket, byte[] bytes);
}
