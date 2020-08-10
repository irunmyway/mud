package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.net.Player;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.PlayersSql;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-06 10:02
 * 功能: 玩家缓存
 **/
public class PlayerCache {
    public static HashMap<String, Player> players=new HashMap<>();
    public static void initPlayerCache(){
        players.clear();
        List<Player> playerList = DataBase.getInstance().init().createSQL(PlayersSql).list(Player.class);
        for(Player player :playerList){
            players.put(player.getAccount()+"",player);
        }
        playerList=null;
    }
    public static synchronized void remove(int id){
        players.remove(id+"");
    }

    public static synchronized Player getPlayer(Object account){
        Player player = players.get(account);
        if(player==null){
            initPlayerCache();
            player = players.get(account);
        }
        return player;
    }
}
