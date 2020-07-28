package com.eztv.mud.handler;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.net.Player;

import java.util.Base64;

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
        try {
            String data = new String(Base64.getEncoder().encode(player.getPlayerData().toJson().getBytes()));
            String sql ="update role set data='"+ data+
                    "' where account = "+player.getAccount();
            DataBase.getInstance().init().createSQL(sql).update();
        }catch (Exception e){e.printStackTrace();}
    }
}
