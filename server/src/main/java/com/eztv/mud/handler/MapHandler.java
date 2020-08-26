package com.eztv.mud.handler;

import com.eztv.mud.Constant;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.Room;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.RoomDetail;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.syn.WordSyn;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.Constant.脚本_事件_进入房间;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.*;

public class MapHandler {

    //玩家移动指令【上下左右】
    public static void playerMove(Client client, Msg msg) {//玩家移动模块
        Room outRoom = client.getPlayer().getPlayerData().getRoom();
        Room targetRoom = null;
        Room curRoom = getCacheCurRoom((client));
        switch (msg.getMsg()) {
            case "left":
                targetRoom = getRoom(curRoom.getMap() + "", curRoom.getLeft() + "");
                break;
            case "right":
                targetRoom = getRoom(curRoom.getMap() + "", curRoom.getRight() + "");
                break;
            case "top":
                targetRoom = getRoom(curRoom.getMap() + "", curRoom.getTop() + "");
                break;
            case "down":
                targetRoom = getRoom(curRoom.getMap() + "", curRoom.getDown() + "");
                break;
        }
        if (!outRoom.equals(targetRoom) && targetRoom != null) {
            if (targetRoom.getName().length() < 1) return;
            //执行lua 预处理脚本
            WinMessage win = new WinMessage();
            boolean flag = false;
            try {
                flag = (Boolean) client.getScriptExecutor().load(getRoom(targetRoom).getScript())
                        .execute(脚本_事件_进入房间, client, win, msg);
            } catch (Exception e) {
                flag = true;
            }
            if (flag == true) {//代表允许进入
                changeRoom(client, outRoom, targetRoom);
            }
        }
        targetRoom = null;
    }

    //玩家通过map - id飞到某个房间
    public static boolean goRoom(Client client, String map, String id) {
        Room outRoom = client.getPlayer().getPlayerData().getRoom();
        if (outRoom.isFly() == false) {//地图不允许飞行
            GameUtil.sendSystemMsg(client, getProp("room_no_fly"));
            return false;
        }
        Room targetRoom = getRoom(map, id);
        if (outRoom.equals(targetRoom) || targetRoom.getName().length() < 1) return false;
        //执行lua 预处理脚本
        WinMessage win = new WinMessage();
        boolean flag = false;
        try {
            flag = (Boolean) client.getScriptExecutor().load(getRoom(targetRoom).getScript())
                    .execute(脚本_事件_进入房间, client, win, new Msg());
        } catch (Exception e) {
            flag = true;
        }
        if (flag == true) {//代表允许进入
            changeRoom(client, outRoom, targetRoom);
        }
        targetRoom = null;
        return true;
    }


    public static void changeRoom(Client client, Room outRoom, Room targetRoom) {
        //设置到达新房间
        client.getPlayer().getPlayerData().setRoom(targetRoom);
        //退出房间 并到达新房间
        if (client.getPlayer().getPlayerData().getRoom() != null) {
            onObjectOutRoom(outRoom, client.getPlayer());
            getMapDetail(client);
        }
    }

    public static void getMapDetail(Client client) {//查看房间
        Room roomId = getCacheCurRoom(client);
        onObjectOutRoom(roomId, client.getPlayer());
        client.getPlayer().getPlayerData().setRoom(roomId);//进入房间
        WordSyn.InOutRoom(client.getPlayer(), roomId, true);
        RoomDetail room = new RoomDetail();
        for (GameObject obj : getRoom(roomId).getPlayerList()) {
            if (client.getPlayer().equals(obj)) continue;
            room.addGameObject(obj.toSendGameObject());
        }
        for (GameObject obj : getRoom(roomId).getNpcList()) {
            room.addGameObject(obj.toSendGameObject());
        }
        for (GameObject obj : getRoom(roomId).getMonsterList()) {
            room.addGameObject(obj.toSendGameObject());
        }
        room.setName(getRoom(roomId).getName());
        room.setDown(getRoomName(getRoom(roomId).getMap() + "", getRoom(roomId).getDown()));
        room.setLeft(getRoomName(getRoom(roomId).getMap() + "", getRoom(roomId).getLeft()));
        room.setRight(getRoomName(getRoom(roomId).getMap() + "", getRoom(roomId).getRight()));
        room.setTop(getRoomName(getRoom(roomId).getMap() + "", getRoom(roomId).getTop()));
        client.sendMsg(msgBuild(Enum.messageType.action, getMapDetail, object2JsonStr(room), client.getRole()).getBytes());

        //通知其他玩家；
        onObjectInRoom(roomId, client.getPlayer(), client);
    }

    //告诉玩家我来了
    private static void onObjectInRoom(Room roomId, GameObject obj, Client client) {
        for (Client item : clients) {
            try {
                if (item.getPlayer().getPlayerData().getRoom().equals(roomId) && item != client) {
                    item.sendMsg(msgBuild(Enum.messageType.normal, onObjectInRoom, object2JsonStr(obj.toSendGameObject()), "").getBytes());
                }
            } catch (Exception e) {
            }
        }
    }

    //告诉玩家我走了
    public static void onObjectOutRoom(Room room, Player player) {
        if (room == null) {
            room = GameUtil.getRoom(Constant.DEFAULT_MAP_ID, Constant.DEFAULT_ROOM_ID);
        }
        WordSyn.InOutRoom(player, room, false);
        for (Client item : clients) {
            try {
                if (item.getPlayer().getPlayerData().getRoom().equals(room) && item.getPlayer() != player)
                    item.sendMsg(msgBuild(Enum.messageType.normal, onObjectOutRoom, object2JsonStr(player.toSendGameObject()), null).getBytes());
            } catch (Exception e) {
            }
        }
    }


}
