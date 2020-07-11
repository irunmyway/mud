package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.WinMessage;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.bean.Cmd.chat;

public class ChatHandler {
    public static void doChatWin(Client client, Msg msg) {//发送聊天窗口
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        winMsg.setDesc(msg.getRole());
        choice.add(Choice.createChoice("发送", Enum.messageType.chat,msg.getCmd(), msg.getMsg()));
        winMsg.setChoice(choice);
        GameObject gameObject = GameUtil.getGameObject(client,msg.getRole());
        String name ="";//发给谁
        if(gameObject!=null)name = gameObject.getName();
        //聊天窗口 对方id  对方名字
        client.sendMsg(msgBuild(Enum.messageType.input, chat,object2JsonStr(winMsg),msg.getRole(),name).getBytes());
    }
    public static void doChat(Client client, Msg msg) {//发送聊天内容
        Chat chat = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), Chat.class);

        switch (msg.getCmd()){
            case "公聊":
                for (Client item: clients) {
                    item.sendMsg(msgBuild(Enum.messageType.chat, msg.getCmd(),object2JsonStr(chat),"").getBytes());
                }
                break;
            case "私聊":
                for (Client item: clients) {
                    try{
                        if(item.getPlayer().getKey().equals(msg.getRole())){
                            chat.setToName(item.getPlayer().getName());
                            chat.setTo(item.getPlayer().getKey());
                            item.sendMsg(msgBuild(Enum.messageType.chat, msg.getCmd(),object2JsonStr(chat),"").getBytes());
                            if(item.equals(client))return;
                        }
                    }catch (Exception e){}
                }
                for (Client item: clients) {
                    try{
                        if(item.equals(client)){
                            item.sendMsg(msgBuild(Enum.messageType.chat, msg.getCmd(),object2JsonStr(chat),"").getBytes());
                        }
                    }catch (Exception e){}
                }
                break;
        }

    }
}
