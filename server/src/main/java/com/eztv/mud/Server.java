package com.eztv.mud;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.command.CommandSetHandler;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Cmd;
import com.eztv.mud.handler.GameHandler;
import com.eztv.mud.handler.LoginHandler;
import com.eztv.mud.socket.SocketServer;
import com.eztv.mud.socket.callback.SocketServerCallback;
import com.eztv.mud.syn.WordSyn;
import com.eztv.mud.utils.BDebug;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.Constant.通信检查;
import static com.eztv.mud.GameUtil.getAttribute;
import static com.eztv.mud.handler.ChatHandler.doChat;
import static com.eztv.mud.handler.ChatHandler.doChatWin;
import static com.eztv.mud.handler.MapHandler.*;

public class Server implements SocketServerCallback {

    private static Server Instance;
    private int port = 1314;

    //获取单例
    public static Server getInstance() {
        if (Instance == null) {
            Instance = new Server();
        }
        return Instance;
    }

    public void init() {
        new Thread(() -> SocketServer.getInstance().Build(port).
                setMaxReceiveMB(5).
                setSocketServerCallback(Server.this).
                run()).
                start();
        BDebug.trace("Socket build:Success");
    }

    public void onError(Exception e) {
        e.printStackTrace();
        BDebug.trace("onError" + e.toString());
    }

    public void onClosed(ServerSocket serverSocket, Socket socket, Client client) {
        try {
            //保存测试
            client.getPlayer().getDataBaseHandler().saveAll(client.getPlayer());
            clients.remove(socket);
        } catch (Exception e) {
        }
        try {
            onObjectOutRoom(client.getPlayer().getPlayerData().getRoom(), client.getPlayer());
            WordSyn.InOutRoom(client.getPlayer(), client.getPlayer().getPlayerData().getRoom(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (通信检查)
            BDebug.trace("onClosed");
    }

    public void onConnected(Socket socket) {
        //连接成功添加包装Client
        Client client = new Client(socket, new Player());
        clients.add(client);
        if (通信检查)
            BDebug.trace("onConnected");
    }

    public void onBuildFail(ServerSocket serverSocket, boolean needReconnect) {
    }

    public void onReceive(Client client, byte[] bytes) {
        String jsonStr = new String(bytes);
        if (通信检查)
            BDebug.trace("onReceive:" + jsonStr);
        JSONObject json = new JSONObject();
        try {
            json = JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            return;
        }
        if (!json.getString("type").equals("login") && client.getPlayer().getKey() == null) {
            try {
                client.getSocket().close();
            } catch (IOException e) {
            }
        }
        switch (json.getString("type")) {
            case "login"://登录处理
                LoginHandler.login(client, json);
                break;
            case "normal"://普通消息
                Msg msg = JSONObject.toJavaObject(json, Msg.class);
                switch (msg.getCmd()) {
                    case Cmd.getGG://获取房间信息
                        GameHandler.getGG(client);
                        break;
                }
                break;
            case "action"://指令
                msg = JSONObject.toJavaObject(json, Msg.class);
                switch (msg.getCmd()) {
                    case Cmd.getMapDetail://获取房间信息
                        getMapDetail(client);
                        break;
                    case Cmd.playerMove://玩家移动
                        playerMove(client, msg);
                        break;
                    case Cmd.getAttribute://获取玩家属性
                        getAttribute(client);
                        break;
                    case Cmd.doAttack://获取房间信息
                        GameHandler.doAttack(client, msg);
                        break;
                    case Cmd.doTalk://玩家对话
                        GameHandler.doTalk(client, msg);
                        break;
                    default:
                        //指派类去执行
                        String key = msg.getCmd();
                        if (msg.getCmd().contains("item_")) key = "item_action";//物品操作集合
                        if (CommandSetHandler.actionCommandSet.containsKey(key)) {
                            Class clazz = CommandSetHandler.actionCommandSet.get(key);
                            try {
                                Constructor c = clazz.getConstructor(Client.class, Msg.class, String.class);
                                BaseCommand command = (BaseCommand) c.newInstance(client, msg, key);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
                break;
            case "input"://发送请求输入框
                msg = JSONObject.toJavaObject(json, Msg.class);
                switch (msg.getCmd()) {
                    case "私聊":
                    case "公聊":
                        doChatWin(client, msg);
                        break;
                }
                break;
            case "chat"://发送聊天内容
                msg = JSONObject.toJavaObject(json, Msg.class);
                switch (msg.getCmd()) {
                    case "私聊":
                    case "公聊":
                        doChat(client, msg);
                        break;
                }
                break;
            case "pop"://弹窗
                msg = JSONObject.toJavaObject(json, Msg.class);
                //指派类去执行
                if (CommandSetHandler.panelCommandSet.containsKey(msg.getCmd())) {
                    Class clazz = CommandSetHandler.panelCommandSet.get(msg.getCmd());
                    try {
                        Constructor c = clazz.getConstructor(Client.class, Msg.class, String.class);
                        BaseCommand command = (BaseCommand) c.newInstance(client, msg, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
