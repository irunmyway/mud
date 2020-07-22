package com.eztv.mud.handler;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.net.Player;

public class DataBaseHandler {
    public void saveAll(Player player){
        savePlayer(player);
        savePlayerData(player);
    }
    public void savePlayer(Player player){
        try {
            DataBase.getInstance().init().query(player).setExceptFields("name").update();
        }catch (Exception e){e.printStackTrace();}
    }
    public void savePlayerData(Player player){
        String sql ="update role set data='"+player.getPlayerData().toJson()+
                "' where account = "+player.getAccount();
        try {
            DataBase.getInstance().init().createSQL(sql).update();
        }catch (Exception e){e.printStackTrace();}
    }
}
