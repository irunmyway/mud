package com.eztv.mud;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.*;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.cache.MonsterCache;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BObject;
import com.eztv.mud.utils.BProp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        List<GameObject> list = new ArrayList<>();
        list.addAll(getCurRoom(client).getMonsterList());
        list.addAll(getCurRoom(client).getNpcList());
        list.addAll(getCurRoom(client).getPlayerList());
        for (GameObject o:list) {
            if(o.getKey().equals(key)){
                return o;
            }
        }
        return null;
    }
    //通过静态id查找游戏怪物
    public static GameObject getMonstertById(String id) {
        GameObject gameObject=null;
        List<Monster> list = MonsterCache.getMonsters();
        for (GameObject o:list) {
            if((o.getId()+"").equals(id)){
                gameObject = o;
                break;
            }
        }
        return gameObject;
    }




    //获取玩家属性
    public static void getAttribute(Client client) {
        client.sendMsg(msgBuild(Enum.messageType.action, getAttribute,object2JsonStr(client.getPlayer().getPlayerData().getAttribute()),client.getRole()).getBytes());
    }

    //查看玩家自身
    public static void getSelf(Client client) {
        client.sendMsg(msgBuild(Enum.messageType.action, "getSelf",object2JsonStr(client.getPlayer().toSendGameObject()),client.getRole()).getBytes());
    }

    public static Item findItemById(int id) {
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
    public static void sendToFaction(int faction,String str){//发送帮派
        for (Client item: clients) {
            if(FactionCache.factions.containsKey(faction+"")){
                try{
                    item.sendMsg(str.getBytes());
                }catch (Exception e){e.printStackTrace();}
            }
        }
    }
    public static void sendToRoom(Client client,String str){//向该地图的人发送战斗内容
        for (Client item : clients) {//向该地图的人发送战斗内容
            if (BObject.isNotEmpty(item.getPlayer().getPlayerData().getRoom()) && BObject.isNotEmpty(client.getPlayer().getPlayerData().getRoom()))
                if (item.getPlayer().getPlayerData().getRoom().equals(client.getPlayer().getPlayerData().getRoom()))
                    item.sendMsg(str.getBytes());
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
    public static void sendToKey(String key,String str){//发送给自己
        for (Client item: clients) {
            try{
                if(item.getPlayer().getKey().equals(key)){
                    item.sendMsg(str.getBytes());
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }
    public static String getProp(String prop,Object... args){
        Properties Config = BProp.getInstance().getProp();
        return colorString(String.format(Config.get(prop).toString(),args));
    }
    public static String getPropByFile(String file,String prop,Object... args){
        Properties Config = BProp.getInstance().getProp(file);
        if(Config==null)return "";
        return colorString(String.format(Config.get(prop).toString(),args));
    }

    public static String colorString(String str){
        if(str!=null)
            if(str.contains("<")){
                if(str.contains("<red>"))str = str.replaceAll("<red>","<font color=\"#C02E2E\">");
                if(str.contains("<blue>"))str = str.replaceAll("<blue>","<font color=\"#4682B4\">");
                if(str.contains("<green>"))str = str.replaceAll("<green>","<font color=\"#32CD32\">");
                if(str.contains("<gray>"))str = str.replaceAll("<gray>","<font color=\"#BDB76B\">");
                if(str.contains("<yellow>"))str = str.replaceAll("<yellow>","<font color=\"#FFD700\">");
                if(str.contains("<purple>"))str = str.replaceAll("<purple>","<font color=\"#8a6bbe\">");
                if(str.contains("<white>"))str = str.replaceAll("<white>","<font color=\"#ffffff\">");
                if(str.contains("<pink>"))str = str.replaceAll("<pink>","<font color=\"#FF69B4\">");
                if(str.contains("<brown>"))str = str.replaceAll("<brown>","<font color=\"#52433d\">");
                if(str.contains("<jb>"))str = str.replaceAll("<jb>","<font color=\"#FFD700\">");
                if(str.contains("<yb>"))str = str.replaceAll("<yb>","<font color=\"#DAA520\">");
                if(str.contains("<money>"))str = str.replaceAll("<money>","<font color=\"#D2691E\">");

                if(str.contains("</>"))str = str.replaceAll("</>","</font>");
            }
        return str;
    }
}
