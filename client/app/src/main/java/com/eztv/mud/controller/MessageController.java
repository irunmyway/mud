package com.eztv.mud.controller;

import com.alibaba.fastjson.JSONObject;
import com.ez.socket.SocketClient;
import com.ez.utils.BDebug;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.AttackPack;

import static com.eztv.mud.bean.Cmd.*;
import static com.eztv.mud.util.Util.msgBuild;
import static com.eztv.mud.util.Util.object2JsonStr;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-05 20:48
 * 功能: 发送给服务器消息的集合
 **/
public class MessageController {

    public static void send(Object obj){//如果断开了连接就不要发送了
        System.out.println("开始返送");
        SocketClient.getInstance().sendMsgByLength(JSONObject.parseObject(JSONObject.toJSON(obj).toString()) .toJSONString().getBytes());
        System.out.println("发送完毕");
    }

    //第一次进游戏获取地图信息
    public static void getMapDetail(){
        Msg msg = new Msg();
        msg.setType(Enum.messageType.action);
        msg.setCmd(getMapDetail);
        send(msg);
    }

    //第一次进游戏获取地图信息
    public static void gotByDirect(Enum.direct direct){
        Msg msg = new Msg();
        msg.setType(Enum.messageType.action);
        msg.setCmd(playerMove);
        msg.setMsg(direct.toString());
        send(msg);
    }

    //聊天模块
    public static void doChat(Chat chat){
        send(msgBuild(Enum.messageType.chat, doChat,object2JsonStr(chat),""));
    }

    //获取玩家属性
    public static void getAttribute(){
        send(msgBuild(Enum.messageType.action, getAttribute,"",""));
    }
    //攻击
    public static void doAttack(Enum.gameObjectType gameObjectType,String target){
        AttackPack  attackPack = new AttackPack();
        attackPack.setTargetType(gameObjectType);
        attackPack.setTarget(target);
        send(msgBuild(Enum.messageType.action, doAttack,object2JsonStr(attackPack),""));
    }
    //获取玩家属性
    public static void getGG(){//获取公告
        send(msgBuild(Enum.messageType.normal, getGG,"",""));
    }

    //对话 游戏对象
    public static void doTalk(String key){//获取公告
        send(msgBuild(Enum.messageType.action, doTalk,key,""));
    }

}
