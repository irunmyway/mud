package com.ez.socket.callback;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-06-26 9:44
 * 功能: 套接字基础监听
 **/
public interface SocketServerCallback {
    //出错
    void onError(Exception e);

    //关闭
    void onClosed(ServerSocket serverSocket, Socket socket);

    //已连接
    void onConnected(Socket socket);

    //服务失败
    void onBuildFail(ServerSocket serverSocket, boolean needReconnect);

    //接收
    void onReceive(Socket socket, byte[] bytes);
}
