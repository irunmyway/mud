package com.eztv.mud;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Cmd;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.handler.GameHandler;
import com.eztv.mud.handler.LoginHandler;
import com.eztv.mud.socket.SocketServer;
import com.eztv.mud.socket.callback.SocketServerCallback;
import com.eztv.mud.utils.BDebug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.getAttribute;
import static com.eztv.mud.handler.ChatHandler.doChat;
import static com.eztv.mud.handler.ChatHandler.doChatWin;
import static com.eztv.mud.handler.MapHandler.*;

public class Server implements SocketServerCallback {

    private int port  = 1314;
    private static Server Instance;
    //获取单例
    public static Server getInstance() {
        if (Instance == null) {
            Instance = new Server();
        }
        return Instance;
    }

    public void init() {
        new Thread(new Runnable() {
            public void run() {
                SocketServer.getInstance().Build(port).setMaxReceiveMB(5).setSocketServerCallback(Server.this).run();
            }
        }).start();
        BDebug.trace("Socket build:Success");
    }

    public void onError(Exception e) {e.printStackTrace();BDebug.trace("onError"+e.toString());}

    public void onClosed(ServerSocket serverSocket,Socket socket,Client client) {
        try {
            onObjectOutRoom(client.getPlayer().getPlayerData().getRoom(),client.getPlayer());
            Word.getInstance().getRooms().get(client.getPlayer().getPlayerData().getRoom()).remove(client.getPlayer());
        }catch (Exception e){e.printStackTrace();}
        if(通信检查)
        BDebug.trace("onClosed");
    }

    public void onConnected(Socket socket) {
        //连接成功添加包装Client
        Client client = new Client(socket,new Player());
        clients.add(client);
        if(通信检查)
        BDebug.trace("onConnected");
    }

    public void onBuildFail(ServerSocket serverSocket, boolean needReconnect) {}

    public void onReceive(Client client, byte[] bytes) {
        String jsonStr = new String(bytes);
        if(通信检查)
        BDebug.trace("onReceive:"+jsonStr);
        JSONObject json = new JSONObject();
        try{
            json = JSONObject.parseObject(jsonStr);
        }catch (Exception e){return;}
        if(!json.getString("type").equals("login")&&client.getPlayer().getKey()==null){
            try {client.getSocket().close(); } catch (IOException e) {}
        }
        switch (json.getString("type")){
            case "login"://登录处理
                LoginHandler.login(client,json);
                break;
            case "normal"://普通消息
                Msg msg =  JSONObject.toJavaObject(json,Msg.class);
                switch (msg.getCmd()){
                    case Cmd.getGG://获取房间信息
                        GameHandler.getGG(client);
                        break;
                }
                break;
            case "action"://指令
                msg =  JSONObject.toJavaObject(json,Msg.class);
                switch (msg.getCmd()){
                    case Cmd.getMapDetail://获取房间信息
                        getMapDetail(client);
                        break;
                    case Cmd.playerMove://玩家移动
                        playerMove(client,msg);
                        break;
                    case Cmd.getAttribute://获取玩家属性
                        getAttribute(client);
                        break;
                    case Cmd.doAttack://获取房间信息
                        GameHandler.doAttack(client,msg);
                        break;
                    case Cmd.doTAlk://玩家对话
                        GameHandler.doTalk(client,msg);
                        break;
                    case "relive"://玩家复活
                        GameHandler.relive(client,msg);
                        break;

                }
                break;
            case "input"://发送请求输入框
                msg =  JSONObject.toJavaObject(json,Msg.class);
                switch (msg.getCmd()){
                    case "私聊":
                    case "公聊":
                        doChatWin(client,msg);
                        break;
                }
                break;
            case "chat"://发送聊天内容
                msg =  JSONObject.toJavaObject(json,Msg.class);
                switch (msg.getCmd()) {
                    case "私聊":
                    case "公聊":
                        doChat(client, msg);
                        break;
                }
            break;
        }
    }
}
