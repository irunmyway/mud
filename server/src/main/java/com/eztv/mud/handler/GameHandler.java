package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.PropertiesUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.constant.Enum.*;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.Word;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.GameUtil.getGameObject;
import static com.eztv.mud.constant.Cmd.*;

public class GameHandler {

    //玩家攻击
    public static void doAttack(Client client,Msg msg) {//重写。。。。获取所有列表从里头拿
        AttackPack attackPack = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), AttackPack.class);
        GameObject gameObject=getGameObject(client,attackPack.getTarget());
        client.getPlayer().setBattle(client,client.getPlayer(),gameObject,attackPack.getTarget());
    }

    //获取公告
    public static void getGG(Client client) {
        Chat chat = new Chat(Enum.chat.系统,Word.getInstance().getGG());
        client.sendMsg(msgBuild(messageType.normal, getGG,object2JsonStr(chat),"").getBytes());
    }

    //    对话object
    public static void doTalk(Client client, Msg msg) {
        WinMessage winMsg = new WinMessage();
        GameObject gameObject=getGameObject(client,msg.getMsg());
        if(gameObject==null)return;
        List<Choice> choice = new ArrayList<>();
        if(gameObject instanceof Player){//是玩家
            choice.add(Choice.createChoice("私聊", messageType.input,"私聊", gameObject.getKey(),null));
            winMsg.setChoice(choice);
            winMsg.setDesc(gameObject.getName());
            client.sendMsg(msgBuild(messageType.action, doTalk,object2JsonStr(winMsg),gameObject.getKey()).getBytes());
        }else{
            client.getScriptExecutor().loadFile(null,gameObject.getScript() + ".lua")
                    .execute("talk",client,winMsg,msg,gameObject);
        }
    }



}
