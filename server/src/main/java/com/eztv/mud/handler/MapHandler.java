package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.RoomDetail;

import java.util.List;

import static com.eztv.mud.Constant.DEFAULT_ROOM_ID;
import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.bean.Cmd.*;

public class MapHandler {
    public static void getMapDetail(Client client) {//查看房间
        String roomId = getCurRoomId(client);
        client.getPlayer().getPlayerData().setRoom(roomId);//进入房间
        Word.getInstance().getRooms().get(roomId + "").add(client.getPlayer());//清除其他房间自己的镜像
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
        Word.getInstance().getRooms().get(roomId + "").remove(player);
        for (Client item : clients) {
            try {
                if (item.getPlayer().getPlayerData().getRoom().equals(roomId) && item.getPlayer() != player)
                    item.sendMsg(msgBuild(Enum.messageType.normal, onObjectOutRoom, object2JsonStr(player.toSendGameObject()), null).getBytes());
            } catch (Exception e) {
            }
        }
    }

    public static void playerMove(Client client, Msg msg) {//玩家移动模块
        onObjectOutRoom(client.getPlayer().getPlayerData().getRoom(), client.getPlayer());
        switch (msg.getMsg()) {
            case "left":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getLeft() + "");
                break;
            case "right":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getRight() + "");
                break;
            case "top":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getTop() + "");
                break;
            case "down":
                client.getPlayer().getPlayerData().setRoom(getRoom(getCurRoomId((client))).getDown() + "");
                break;
        }
        getMapDetail(client);
    }
}
