package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.constant.Enum.*;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.Word;
import com.eztv.mud.bean.net.WinMessage;
import org.luaj.vm2.LuaValue;

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
            winMsg.setDesc(gameObject.getName()+"</p><br>&emsp;"+"这是一位凶神恶煞的玩家");
        }else{
            client.getScriptExecutor().loadfile(gameObject.getScript() + ".lua").call();
            for (Object c:JSONObject.toJavaObject(jsonStr2JsonArr(client.getScriptExecutor().get(LuaValue.valueOf("choice")).invoke().toString()), List.class) ) {
                Choice co = JSONObject.toJavaObject(JSONObject.parseObject(JSONObject.toJSON(c).toString()), Choice.class);
                choice.add(co);
            }
            winMsg.setChoice(choice);
            winMsg.setDesc(gameObject.getName()+"</p><br>&emsp;"+(gameObject.getDesc()==null?"":gameObject.getDesc()));
        }
        client.sendMsg(msgBuild(messageType.action, doTAlk,object2JsonStr(winMsg),gameObject.getKey()).getBytes());
    }

    //复活
    public static void relive(Client client, Msg msg) {
        DataHandler. getPlayer(client,client.getPlayer());
        MapHandler.getMapDetail(client);
        getAttribute(client);
    }
}
