package com.eztv.mud.socket.callback;

import com.eztv.mud.bean.Client;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EmptyCallbackImpl implements SocketCallback, HeartbeatCallback, SocketServerCallback {

    public void onStopHeartbeat() {

    }

    public void onPreSend(DataOutputStream out) {

    }

    public void onError(Exception e) {

    }

    @Override
    public void onClosed(ServerSocket serverSocket, Socket socket, Client client) {

    }

    public void onClosed(Socket socket) {

    }

    public void onConnected(Socket socket) {

    }

    public void onBuildFail(ServerSocket serverSocket, boolean needReconnect) {

    }

    @Override
    public void onReceive(Client client, byte[] bytes) {

    }

    public void onConnectFail(Socket socket, boolean needReconnect) {

    }

    @Override
    public void onReceive(Socket socket, byte[] bytes) {

    }

}
