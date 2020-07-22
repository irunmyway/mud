package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BString;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.chat;

public class ChatHandler {
    public static void doChatWin(Client client, Msg msg) {//发送聊天窗口
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        winMsg.setDesc(msg.getRole());
        choice.add(Choice.createChoice("发送", Enum.messageType.chat,msg.getCmd(), msg.getMsg(),msg.getMsg()));
        winMsg.setChoice(choice);
        GameObject gameObject = GameUtil.getGameObject(client,msg.getRole());
        String name ="";//发给谁
        if(gameObject!=null) name = gameObject.getName();
        //聊天窗口 对方id  对方名字
        client.sendMsg(msgBuild(Enum.messageType.input, chat,object2JsonStr(winMsg),msg.getMsg(),name).getBytes());
    }
    public static void doChat(Client client, Msg msg) {//发送聊天内容
        Chat chat = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), Chat.class);

        switch (msg.getCmd()){
            case "公聊":
                chat.setContent(BString.getHTMLStr(chat.getContent()));
                sendToAll(client,msgBuild(Enum.messageType.chat, msg.getCmd(),object2JsonStr(chat),""));
                break;
            case "私聊":
                chat.setContent(BString.getHTMLStr(chat.getContent()));
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
                sendToSelf(client,msgBuild(Enum.messageType.chat, msg.getCmd(),object2JsonStr(chat),""));
                break;
        }

    }
}
