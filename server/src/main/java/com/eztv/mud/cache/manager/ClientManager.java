package com.eztv.mud.cache.manager;

import com.eztv.mud.Constant;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Room;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.PlayerCache;
import com.eztv.mud.cache.RoomCache;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.handler.MapHandler;
import com.eztv.mud.utils.BDate;

import static com.eztv.mud.GameUtil.getAttribute;
import static com.eztv.mud.GameUtil.getProp;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-17 23:16
 * 功能: 客户端和 角色的管理控制
 **/
public class ClientManager {
    public static Client getClient(Object key) {
        for (Client client : Constant.clients) {
            if (client.getPlayer().getKey().equals(key))
                return client;
        }
        return null;
    }

    public static Client getClientByAccount(Object account) {
        for (Client client : Constant.clients) {
            if (client.getPlayer().getAccount().equals(account))
                return client;
        }
        return null;
    }

    public static Room getCurRoom(Client client) {
        Room room = null;
        try {
            room = RoomCache.getRoomsByMap(client.getPlayer().getPlayerData().getRoom().getMap() + "").get(client.getPlayer().getPlayerData().getRoom().getId() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return room == null ? new Room() : room;
    }

    //判断玩家是否在线
    public static Player isOnline(Player player) {
        for (Client client : Constant.clients) {
            if (client.getPlayer().getAccount().equals(player.getAccount())){
                return client.getPlayer();
            }
        }
        return null;
    }

    //判断角色是否死亡
    public static long isDead(Client client, Player player) {
        if (player == null) return -1;
        if (player.getAccount() == null) return -1;
        Player cachePlayer = PlayerCache.getPlayer(player.getAccount());
        if (cachePlayer == null) return -1;
        long deadTime = cachePlayer.getDeadTime();
        if (deadTime > 1) {
            long second = (BDate.getNowMills() - deadTime) / 1000;
            int span = Integer.parseInt(getProp("reliveSpan"));
            if (second > span) {//复活了
                DataHandler.getPlayer(client, player);
                MapHandler.getMapDetail(client);
                getAttribute(client);
                return -1;
            } else {
                return span - second;
            }
        } else {//没死
            return -1;
        }
    }

}
