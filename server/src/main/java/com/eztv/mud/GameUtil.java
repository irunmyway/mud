package com.eztv.mud;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.DEFAULT_ROOM_ID;
import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.constant.Cmd.getAttribute;

public class GameUtil {
    //通过房间id获取房间实体
    public static Room getRoom(String roomId) {
        Room room = Word.getInstance().getRooms().get(roomId);
        return room == null ? new Room() : room;
    }

    //通过房间id获取房间名称
    public static String getRoomName(int roomId) {
        Room room = Word.getInstance().getRooms().get(roomId + "");
        return room == null ? "" : room.getName();
    }

    //Msg消息流生成器
    public static String msgBuild(Enum.messageType type, String cmd, String msgstr, String role) {
        Msg msg = new Msg();
        msg.setCmd(cmd);
        msg.setMsg(msgstr);
        msg.setRole(role);
        msg.setType(type);
        return object2JsonStr(msg);
    }

    //Msg消息流生成器
    public static byte[] msgBuildForBytes(Enum.messageType type, String cmd, String msgstr, String role) {
        Msg msg = new Msg();
        msg.setCmd(cmd);
        msg.setMsg(msgstr);
        msg.setRole(role);
        msg.setType(type);
        return object2JsonStr(msg).getBytes();
    }

    //Msg消息流生成器
    public static String msgBuild(Enum.messageType type, String cmd, String msgstr, String role,String name) {
        Msg msg = new Msg();
        msg.setCmd(cmd);
        msg.setMsg(msgstr);
        msg.setRole(role);
        msg.setType(type);
        msg.setName(name);
        return object2JsonStr(msg);
    }

    //Msg消息流生成器
    public static byte[] msgBuildForBytes(Enum.messageType type, String cmd, String msgstr, String role,String name) {
        Msg msg = new Msg();
        msg.setCmd(cmd);
        msg.setMsg(msgstr);
        msg.setRole(role);
        msg.setType(type);
        msg.setName(name);
        return object2JsonStr(msg).getBytes();
    }

    //元素到json
    public static String object2JsonStr(Object obj) {
        return JSONObject.parseObject(JSONObject.toJSON(obj).toString()).toJSONString();
    }

    public static JSONObject jsonStr2Json(String obj){
        return JSONObject.parseObject(JSONObject.toJSON(obj).toString());
    }

    public static String objectArr2JsonStr(Object obj) {
        return JSONObject.parseArray(JSONObject.toJSON(obj).toString()).toJSONString();
    }

    public static JSONArray jsonStr2JsonArr(String obj){
        return JSONObject.parseArray(JSONObject.toJSON(obj).toString());
    }

    //获取玩家当前房间
    public static String getCurRoomId(Client client) {
        String roomId = client.getPlayer().getPlayerData().getRoom();
        return (roomId == null || roomId.equals("-1")) ? DEFAULT_ROOM_ID : roomId;
    }
    //通过房间id获取房间实体
    public static Room getCurRoom(Client client) {
        Room room = Word.getInstance().getRooms().get(getCurRoomId(client));
        return room == null ? new Room() : room;
    }

    //通过动态id查找游戏元素
    public static GameObject getGameObject(Client client, String key) {
        GameObject gameObject=null;
        List<GameObject> list = new ArrayList<>();
        list.addAll(getCurRoom(client).getMonsterList());
        list.addAll(getCurRoom(client).getNpcList());
        list.addAll(getCurRoom(client).getPlayerList());
        for (GameObject o:list) {
            if(o.getKey().equals(key)){
                gameObject = o;
                break;
            }
        }
        return gameObject;
    }
    //通过静态id查找游戏怪物
    public static GameObject getMonstertById(String id) {
        GameObject gameObject=null;
        List<Monster> list = Word.getInstance().getMonsters();
        for (GameObject o:list) {
            if((o.getId()+"").equals(id)){
                gameObject = o;
                break;
            }
        }
        return gameObject;
    }
    //通过静态id查找游戏物品
    public static Item getItemById(String id) {
        Item item = null;
       for(int i=0;i< Word.getInstance().getItems().size();i++){
           if((Word.getInstance().getItems().get(i).getId()+"").equals(id)){
               item = Word.getInstance().getItems().get(i);
           }
       }
       return item;
    }
    //获取玩家属性
    public static void getAttribute(Client client) {
        client.sendMsg(msgBuild(Enum.messageType.action, getAttribute,object2JsonStr(client.getPlayer().getPlayerData().getAttribute()),client.getRole()).getBytes());
    }

    //查看玩家自身
    public static void getSelf(Client client) {
        client.sendMsg(msgBuild(Enum.messageType.action, "getSelf",object2JsonStr(client.getPlayer().toSendGameObject()),client.getRole()).getBytes());
    }

    public static Item findItemById(int id) {//发送给所有人
        Item item =null;
        for(Item it:Word.getInstance().getItems()){
            if(it.getId()==id)
                item = it;
        }
        return item;
    }



    public static void sendToAll(Client client,String str){//发送给所有人
        for (Client item: clients) {
            try{
                item.sendMsg(str.getBytes());
            }catch (Exception e){e.printStackTrace();}
        }
    }
    public static void sendToSelf(Client client,String str){//发送给自己
        for (Client item: clients) {
            try{
                if(item.equals(client)){
                    item.sendMsg(str.getBytes());
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }
}
