package com.eztv.mud.handler;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.Enum.*;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.RoomDetail;
import com.eztv.mud.Word;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.handler.core.Battle;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.GameUtil.getGameObject;
import static com.eztv.mud.bean.Cmd.*;

public class GameHandler {
    public static void getMapDetail(Client client) {//查看房间
        String roomId = getCurRoomId(client);
        client.getPlayer().getPlayerData().setRoom(roomId);//进入房间
        Word.getInstance().getRooms().get(roomId+"").add(client.getPlayer());//清除其他房间自己的镜像
        RoomDetail room = new RoomDetail();
        room.setNpcList(getRoom(roomId).getNpcList());
        room.setMonsterList(getRoom(roomId).getMonsterList());
        List<GameObject> list = getRoom(roomId).getPlayerList();
        list.remove(client.getPlayer());
        room.setPlayerList(list);
        room.setName(getRoom(roomId).getName());
        room.setDown( getRoomName(getRoom(roomId).getDown()));
        room.setLeft( getRoomName(getRoom(roomId).getLeft()));
        room.setRight( getRoomName(getRoom(roomId).getRight()));
        room.setTop( getRoomName(getRoom(roomId).getTop()));
        client.sendMsg(msgBuild(messageType.action, getMapDetail,object2JsonStr(room),client.getRole()).getBytes());
        list.add(client.getPlayer());
        room.setPlayerList(list);
        //通知其他玩家；
        onObjectInRoom(roomId,client.getPlayer(),client);
    }

    private static void onObjectInRoom(String roomId, GameObject obj,Client client){
        for (Client item: clients) {
            try {
                if(item.getPlayer().getPlayerData().getRoom().equals(roomId)&&item!=client){
                    if (obj instanceof Monster)
                    item.sendMsg(msgBuild(messageType.normal, onObjectInRoom,object2JsonStr(obj),"Monster").getBytes());
                    if (obj instanceof Npc)
                        item.sendMsg(msgBuild(messageType.normal, onObjectInRoom,object2JsonStr(obj),"Npc").getBytes());
                    if (obj instanceof Player)
                        item.sendMsg(msgBuild(messageType.normal, onObjectInRoom,object2JsonStr(obj),"Player").getBytes());
                }
            }catch (Exception e){}
        }
    }

    public static void onObjectOutRoom(String roomId, Player player){
        if(roomId==null)roomId=DEFAULT_ROOM_ID;
        Word.getInstance().getRooms().get(roomId+"").remove(player);
        for (Client item: clients) {
            try {
            if(item.getPlayer().getPlayerData().getRoom().equals(roomId)&&item.getPlayer()!=player)
                item.sendMsg(msgBuild(messageType.normal, onObjectOutRoom,object2JsonStr(player),null).getBytes());
            }catch (Exception e){}
        }
    }

    public static void playerMove(Client client, Msg msg) {//玩家移动模块
        GameHandler.onObjectOutRoom(client.getPlayer().getPlayerData().getRoom(),client.getPlayer());
        switch (msg.getMsg()){
            case "left":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getLeft()+"");
                break;
            case "right":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getRight()+"");
                break;
            case "top":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getTop()+"");
                break;
            case "down":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getDown()+"");
                break;
        }
        getMapDetail(client);
    }

    public static void doChatWin(Client client, Msg msg) {//发送聊天窗口
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        winMsg.setDesc(msg.getRole());
        choice.add(Choice.createChoice("发送", messageType.chat,msg.getCmd(), msg.getMsg()));
        winMsg.setChoice(choice);
        GameObject gameObject = GameUtil.getGameObject(client,msg.getRole());
        String name ="";//发给谁
        if(gameObject!=null)name = gameObject.getName();
        //聊天窗口 对方id  对方名字
        client.sendMsg(msgBuild(messageType.input, chat,object2JsonStr(winMsg),msg.getRole(),name).getBytes());
    }

    public static void doChat(Client client, Msg msg) {//发送聊天内容
        Chat chat = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), Chat.class);

        switch (msg.getCmd()){
            case "公聊":
                for (Client item: clients) {
                    item.sendMsg(msgBuild(messageType.chat, msg.getCmd(),object2JsonStr(chat),"").getBytes());
                }
                break;
            case "私聊":
                for (Client item: clients) {
                    try{
                        if(item.getPlayer().getKey().equals(msg.getRole())){
                            chat.setToName(item.getPlayer().getName());
                            chat.setTo(item.getPlayer().getKey());
                            item.sendMsg(msgBuild(messageType.chat, msg.getCmd(),object2JsonStr(chat),"").getBytes());
                            if(item.equals(client))return;
                        }
                    }catch (Exception e){}
                }
                for (Client item: clients) {
                    try{
                        if(item.equals(client)){
                            item.sendMsg(msgBuild(messageType.chat, msg.getCmd(),object2JsonStr(chat),"").getBytes());
                        }
                    }catch (Exception e){}
                 }
                break;
        }

    }
    //获取玩家属性
    public static void getAttribute(Client client) {
        client.sendMsg(msgBuild(messageType.action, getAttribute,object2JsonStr(client.getPlayer().getPlayerData().getAttribute()),client.getRole()).getBytes());
    }

    //玩家攻击
    public static void doAttack(Client client,Msg msg) {//重写。。。。获取所有列表从里头拿
        AttackPack attackPack = JSONObject.toJavaObject(jsonStr2Json(msg.getMsg()), AttackPack.class);
        GameObject gameObject=getGameObject(client,attackPack.getTarget());
        client.getPlayer().setBattle(client,client.getPlayer(),gameObject,attackPack.getTarget());
    }


    public static void getGG(Client client) {
        Chat chat = new Chat(Enum.chat.系统,Word.getInstance().getGG());
        client.sendMsg(msgBuild(messageType.normal, getGG,object2JsonStr(chat),"").getBytes());
    }

    public static void doTalk(Client client, Msg msg) {
        WinMessage winMsg = new WinMessage();
        GameObject gameObject=getGameObject(client,msg.getMsg());
        if(gameObject==null)return;
        List<Choice> choice = new ArrayList<>();
        if(gameObject instanceof Player){//是玩家
            choice.add(Choice.createChoice("私聊", messageType.input,"私聊", gameObject.getKey()));
            winMsg.setChoice(choice);
            winMsg.setDesc(((Player) gameObject).getName()+"</p><br>&emsp;"+"这是一位凶神恶煞的玩家");
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
}
