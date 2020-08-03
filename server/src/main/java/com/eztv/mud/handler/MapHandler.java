package com.eztv.mud.handler;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.RoomDetail;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.syn.WordSyn;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.LuaValue;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.*;

public class MapHandler {

    public static void playerMove(Client client, Msg msg) {//玩家移动模块
        String outRoom = client.getPlayer().getPlayerData().getRoom();
        String targetRoom = "";
        switch (msg.getMsg()) {
            case "left":
                targetRoom = getRoom(getCurRoomId((client))).getLeft() + "";
                break;
            case "right":
                targetRoom = getRoom(getCurRoomId((client))).getRight() + "";
                break;
            case "top":
                targetRoom = getRoom(getCurRoomId((client))).getTop() + "";
                break;
            case "down":
                targetRoom = getRoom(getCurRoomId((client))).getDown() + "";
                break;
        }
        if(!outRoom.equals(targetRoom)&& targetRoom!=""){
            //执行lua 预处理脚本
            WinMessage win = new WinMessage();
            LuaValue luaValue = client.getScriptExecutor().loadFile(null,getRoom(targetRoom).getScript())
            .execute(LUA_进入房间,client,win,msg);
            BDebug.trace("测试"+luaValue);
            if((luaValue==null?"1":luaValue.toString()).equals("1")||
                    getRoom(targetRoom).getScript()=="") {//代表允许进入
                changeRoom(client, outRoom,targetRoom);
            }
        }
        targetRoom=null;
    }

    public static void changeRoom(Client client, String outRoom,String targetRoom) {
        //设置到达新房间
        client.getPlayer().getPlayerData().setRoom(targetRoom);
        //退出房间 并到达新房间
        if (client.getPlayer().getPlayerData().getRoom() != null) {
            onObjectOutRoom(outRoom, client.getPlayer());
            getMapDetail(client);
        }
    }

    public static void getMapDetail(Client client) {//查看房间
        String roomId = getCurRoomId(client);
        client.getPlayer().getPlayerData().setRoom(roomId);//进入房间
        WordSyn.InOutRoom(client.getPlayer(), roomId, true);
        RoomDetail room = new RoomDetail();
        for (GameObject obj : getRoom(roomId).getNpcList()) {
            room.addGameObject(obj.toSendGameObject());
        }
        for (GameObject obj : getRoom(roomId).getMonsterList()) {
            room.addGameObject(obj.toSendGameObject());
        }
        for (GameObject obj : getRoom(roomId).getPlayerList()) {
            if (client.getPlayer().equals(obj)) continue;
            room.addGameObject(obj.toSendGameObject());
        }
        room.setName(getRoom(roomId).getName());
        room.setDown(getRoomName(getRoom(roomId).getDown()));
        room.setLeft(getRoomName(getRoom(roomId).getLeft()));
        room.setRight(getRoomName(getRoom(roomId).getRight()));
        room.setTop(getRoomName(getRoom(roomId).getTop()));
        client.sendMsg(msgBuild(Enum.messageType.action, getMapDetail, object2JsonStr(room), client.getRole()).getBytes());

        //通知其他玩家；
        onObjectInRoom(roomId, client.getPlayer(), client);
    }

    private static void onObjectInRoom(String roomId, GameObject obj, Client client) {
        for (Client item : clients) {
            try {
                if (item.getPlayer().getPlayerData().getRoom().equals(roomId) && item != client) {
                    item.sendMsg(msgBuild(Enum.messageType.normal, onObjectInRoom, object2JsonStr(obj.toSendGameObject()), "").getBytes());
                }
            } catch (Exception e) {
            }
        }
    }

    public static void onObjectOutRoom(String roomId, Player player) {
        if (roomId == null) roomId = DEFAULT_ROOM_ID;
        WordSyn.InOutRoom(player, roomId, false);
        for (Client item : clients) {
            try {
                if (item.getPlayer().getPlayerData().getRoom().equals(roomId) && item.getPlayer() != player)
                    item.sendMsg(msgBuild(Enum.messageType.normal, onObjectOutRoom, object2JsonStr(player.toSendGameObject()), null).getBytes());
            } catch (Exception e) {
            }
        }
    }


}
