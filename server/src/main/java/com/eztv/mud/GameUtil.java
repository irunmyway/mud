package com.eztv.mud;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;

import java.util.List;

import static com.eztv.mud.Constant.DEFAULT_ROOM_ID;

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

    //查找游戏元素
    public static GameObject getGameObject(Enum.gameObjectType gameObjectType,Object list, String key) {
        if(gameObjectType == Enum.gameObjectType.monster){
            for (Monster monster : ((List<Monster>)list)) {
                if (monster.getKey().equals(key))return monster;
            }
        }
        return null;
    }
}
