package com.eztv.mud.socket;


import com.eztv.mud.utils.BArray;
import com.eztv.mud.utils.BObject;
import com.eztv.mud.utils.BString;
import com.eztv.mud.socket.callback.EmptyCallbackImpl;
import com.eztv.mud.socket.callback.HeartbeatCallback;
import com.eztv.mud.socket.callback.SocketCallback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;


/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-06-26 10:54
 * 功能: 套接字客户端
 **/
public class SocketClient extends Thread {
    SocketAddress address;//连接地址
    private Socket socket = null;//定义客户
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private SocketCallback socketCallback;//套接字回调
    private boolean needReconnect;//需要重连
    private long reconnectDelay;
    private boolean needHeartbeat;//需要心跳
    private int maxReceiveMB = 1;//最大接收字节mb
    private HeartbeatClient heartbeatClient;//心跳线程
    private HeartbeatCallback heartbeatCallback;//心跳回调
    private static SocketClient Instance;//单例模式
    private long keepAliveDelay;//心跳速率
    private boolean isExit;//结束标记

    //获取单例
    public static SocketClient getInstance() {
        if (Instance == null) {
            Instance = new SocketClient();
        }
        return Instance;
    }

    //设置连接属性
    public SocketClient createConnection(String target, String strPort, boolean needHeartbeat) {
        int port = 80;
        try {
            port = Integer.parseInt(strPort);
        } catch (Exception e) {
        }
        this.needReconnect = false;
        this.needHeartbeat = needHeartbeat;
        if (port < 2) return this;
        if (!BString.isIP(target)) return this;
        address = new InetSocketAddress(target, port);
        return this;
    }

    //设置连接属性
    public SocketClient createConnection(String target, String strPort, long reconnectDelay, boolean needHeartbeat) {
        int port = 80;
        try {
            port = Integer.parseInt(strPort);
        } catch (Exception e) {
        }
        this.needReconnect = true;
        this.reconnectDelay = reconnectDelay;
        this.needHeartbeat = needHeartbeat;
        if (port < 2) return this;
        if (!BString.isIP(target)) return this;
        address = new InetSocketAddress(target, port);
        return this;
    }

    //初始化连接
    public void init() {
        try {
            if (socketCallback == null) socketCallback = new EmptyCallbackImpl();//设置空监听
            if (heartbeatCallback == null) heartbeatCallback = new EmptyCallbackImpl();//设置空监听
            socket = new Socket();
            socket.connect(address);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            if (!socket.isOutputShutdown()) {//连接成功。
                socketCallback.onConnected(socket);
                if (needHeartbeat) {//开启心跳
                    if (BObject.isNotEmpty(heartbeatClient)) {
                        heartbeatClient.setExit(true);
                        heartbeatClient = null;
                    }
                    heartbeatClient = new HeartbeatClient();
                    heartbeatClient.keepAliveDelay = keepAliveDelay;
                    heartbeatClient.createHeatbeat(socket, heartbeatCallback).start();
                }
            } else {
                socketCallback.onConnectFail(socket, needReconnect);
            }
        } catch (ConnectException e3) {
            socketCallback.onConnectFail(socket, needReconnect);
        } catch (UnknownHostException e1) {
            socketCallback.onError(e1);
        } catch (IOException ex) {
            socketCallback.onError(ex);
        } catch (IllegalArgumentException e2) {
            socketCallback.onError(e2);
        }
    }

    //设置接收的最大值 默认为1m
    public SocketClient setMaxReceiveMB(int maxReceiveMB) {
        this.maxReceiveMB = maxReceiveMB;
        return this;
    }

    //发送数据
    public void sendMsg(final Object... args) {
        new Thread(new Runnable() {
            public void run() {
                if (BObject.isEmpty(args)) {
                    socketCallback.onError(new Exception("发送了空的消息。"));
                    return;
                }
                if (socket != null && socket.isConnected()) {
                    if (!socket.isOutputShutdown()) {
                        try {
                            for (int i = 0; i < args.length; i++) {
                                try {
                                    Object val = args[i];
                                    if (val instanceof byte[])
                                        out.write((byte[]) val);
                                    if (val instanceof String)
                                        out.writeChars(val.toString());
                                } catch (Exception e) {
                                    socketCallback.onError(e);
                                }
                            }
                            out.flush();
                        } catch (Exception e) {
                            try {
                                socket.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            socketCallback.onError(e);
                        }
                    } else {
                        if (needReconnect) reConnect();
                        socketCallback.onClosed(socket);
                    }
                } else {
                    socketCallback.onConnectFail(socket, needReconnect);
                }
            }
        }).start();
    }

    //重写连接
    public void reConnect() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(reconnectDelay);
                } catch (InterruptedException e1) {
                }
                try {
                    if (BObject.isNotEmpty(in) && BObject.isNotEmpty(out)) {
                        in.close();
                        out.close();
                    }
                    socket.close();
                    socket = null;
                    init();
                } catch (IOException e) {
                    socketCallback.onError(new Exception("重连失败"));
                }
            }
        }).start();
    }

    @Override
    public void run() {
        super.run();
        init();
        try {
            byte[] b = new byte[1024 * maxReceiveMB];
            while (!isExit) {
                if (!socket.isClosed()) {
                    if (socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                            BArray.resetArray(b);
                            in.read(b);
                            if (b[0] != 0) {//有数据接收
                                try {
                                    socketCallback.onReceive(socket, b);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    if (needReconnect) reConnect();
                    socketCallback.onClosed(socket);
                }
            }
        } catch (Exception e) {
            if (needReconnect) {
                socketCallback.onError(new Exception("监听线程异常结束,并重连。"));
                try {
                    Thread.sleep(reconnectDelay);
                } catch (InterruptedException e1) {
                }
                run();
            } else {
                socketCallback.onError(new Exception("监听线程异常结束"));
            }
        }
    }

    //设置结束套接字
    public void destroyInstance() {
        isExit = true;
        Instance = null;
    }

    //设置心跳速率
    public void setHeartbeatDelay(long delay) {
        keepAliveDelay = delay;
    }

    //设置套接字回调
    public SocketClient setSocketCallback(SocketCallback socketCallback) {
        this.socketCallback = socketCallback;
        return this;
    }

    //设置心跳回调
    public SocketClient setHeartbeatCallback(HeartbeatCallback heartbeatCallback) {
        this.heartbeatCallback = heartbeatCallback;
        return this;
    }
}