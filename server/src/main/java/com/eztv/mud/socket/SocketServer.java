package com.eztv.mud.socket;


import com.eztv.mud.bean.Client;
import com.eztv.mud.utils.BArray;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;
import com.eztv.mud.socket.callback.EmptyCallbackImpl;
import com.eztv.mud.socket.callback.SocketServerCallback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.utils.BArray.byteArrayToInt;
import static java.lang.System.arraycopy;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-02 10:03
 * 功能: 套接字服务端
 **/
public class SocketServer extends Thread {
    private static SocketServer Instance;//单例模式
    int port;//连接端口
    boolean isExit;//结束标记
    private SocketServer socketServer = null;//定义服务端
    private ServerSocket serverSocket = null;//定义服务端
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private boolean needRebuild;//需要重连
    private long RebuildDelay;
    private SocketServerCallback socketServerCallback;
    private int maxReceiveMB = 1;//最大接收字节mb
    private final int HEAD_LENGTH=2;//包头长度存储空间

    //获取单例
    public static SocketServer getInstance() {
        if (Instance == null) {
            Instance = new SocketServer();
        }
        return Instance;
    }


    public SocketServer Build(int port) {
        this.port = port;
        return this;
    }

    public SocketServer Build(int port, long RebuildDelay) {
        this.RebuildDelay = RebuildDelay;
        this.needRebuild = true;
        this.port = port;
        return this;
    }

    private void init() {
        if (socketServerCallback == null) socketServerCallback = new EmptyCallbackImpl();//设置空监听
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            socketServerCallback.onBuildFail(serverSocket, needRebuild);
        }
    }


    @Override
    public void run() {
        super.run();
        init();
        try {
            while (!isExit) {
                Socket client = serverSocket.accept();
                socketServerCallback.onConnected(client);
                new HandlerThread(client);
            }
        } catch (Exception e) {
            if (needRebuild) {
                socketServerCallback.onError(new Exception("监听线程异常结束,并重连。"));
                try {
                    Thread.sleep(RebuildDelay);
                } catch (InterruptedException e1) {
                }
                reBuild();
            }
            socketServerCallback.onError(new Exception("监听线程异常结束"));
        }
    }

    //重写建立
    public void reBuild() {
        new Thread(new Runnable(){
            public void run() {
                try {
                    Thread.sleep(RebuildDelay);
                } catch (InterruptedException e1) {
                }
                try {
                    if (BObject.isNotEmpty(in) && BObject.isNotEmpty(out)) {
                        in.close();
                        out.close();
                    }
                    serverSocket.close();
                    serverSocket = null;
                    init();
                } catch (IOException e) {
                    socketServerCallback.onError(new Exception("重连失败"));
                }
            }
        }).start();

    }

    //设置结束套接字
    public void destroyInstance() {
        isExit = true;
        Instance = null;
    }

    //设置套接字回调
    public SocketServer setSocketServerCallback(SocketServerCallback socketServerCallback) {
        this.socketServerCallback = socketServerCallback;
        return this;
    }

    //设置接收的最大值 默认为1m
    public SocketServer setMaxReceiveMB(int maxReceiveMB) {
        this.maxReceiveMB = maxReceiveMB;
        return this;
    }

    private class HandlerThread implements Runnable {
        private Socket socket;

        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        //合并数组
        public byte[] mergeByte(byte[] a, byte[] b, int begin, int end) {
            byte[] add = new byte[a.length + end - begin];
            int i = 0;
            for (i = 0; i < a.length; i++) {
                add[i] = a[i];
            }
            for (int k = begin; k < end; k++, i++) {
                add[i] = b[k];
            }
            return add;
        }

        public void run() {
            try {
                byte[] b = new byte[1024 * maxReceiveMB];
                byte[] body = new byte[0];
                boolean isReceive=false;
                int curLen = 0;//读取进度
                int bodyLen = 0;//报文总长
                byte[] head = new byte[HEAD_LENGTH];
                // 读取客户端数据
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while (!socket.isClosed()){
                        try {
                            //沾包问题临时解决部分///////////////////////////////////////
                            if(HEAD_LENGTH>0){
                                if(byteArrayToInt(head,HEAD_LENGTH)<1){//还没有读取到包头
                                    int tmpLen = 0;
                                    try {
                                        tmpLen  = in.read(b,0,HEAD_LENGTH);
                                    }catch (Exception e){break;}
                                    if(tmpLen>0){
                                        arraycopy(b, 0, head, 0, HEAD_LENGTH);
                                        bodyLen = byteArrayToInt(head,HEAD_LENGTH);
                                    }
                                    body = new byte[bodyLen];
                                    isReceive = true;
                                }
                                if(isReceive){
                                    try {
                                        curLen += in.read(body,0,bodyLen);
                                    }catch (Exception e){break;}
                                    if(curLen==bodyLen&&curLen!=0){
                                        for (Client client: clients) {
                                            if(client.getSocket().equals(socket))socketServerCallback.onReceive(client, body);
                                        }
                                        isReceive = false;
                                        curLen = 0;
                                        head = new byte[HEAD_LENGTH];
                                    }else{
                                        //BDebug.trace("测试"+"关闭了");
                                        break;
                                    }
                                }
                            }else{
                                int len = in.read(b);
                                if (b[0] != 0 && len>0) {//有数据接收
                                    byte[] temp = new byte[len];
                                    arraycopy(b, 0, temp, 0, len);
                                    for (Client client: clients) {
                                        if(client.getSocket().equals(socket))socketServerCallback.onReceive(client, temp);
                                    }
                                }else{
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
                in.close();
                b = null;
                List<Client> preRemoveList = new ArrayList<>();
                for (Client client: clients) {
                    if(client.getSocket().equals(socket)){
                        preRemoveList.add(client);
                        socketServerCallback.onClosed(serverSocket,socket,client);
                    }
                }
                clients.removeAll(preRemoveList);

            } catch (Exception e) {
                socketServerCallback.onError(e);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();//关闭了套接字可能导致回复客户的连接结束
                    } catch (Exception e) {
                        socket = null;
                        socketServerCallback.onError(e);
                    }
                }
            }
        }
    }




}