package com.eztv.mud.handler;

import com.eztv.mud.Constant;
import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.FactionCache;

import java.util.Base64;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-05 7:59
 * 功能: 保存缓存的信息
 **/
public class DataBaseHandler {
    public synchronized void saveAll(Player player){
        savePlayer(player);
        savePlayerData(player);
    }
    public synchronized void savePlayer(Player player){
        try {
            DataBase.getInstance().init().query(player).setExceptFields("name").update();
        }catch (Exception e){e.printStackTrace();}
    }
    //离线更新离线时间
    public void offlinePlayer(Player player){
        try {
            String sql ="update role set updateat=now() where account = ?";
            DataBase.getInstance().init().createSQL(sql,player.getAccount()).update();
        }catch (Exception e){e.printStackTrace();}
    }
    public synchronized void savePlayerData(Player player){
        try {
            String data = new String(Base64.getEncoder().encode(player.getPlayerData().toJson().getBytes()));
            String sql ="update role set data='"+ data+
                    "' where account = ?";
            DataBase.getInstance().init().createSQL(sql,player.getAccount()).update();
        }catch (Exception e){e.printStackTrace();}
    }
    public synchronized void cacheToDataBase(){
      for (Client client:Constant.clients){
          if(client.getPlayer()!=null)
              if(client.getPlayer().getName()!=null)
          saveAll(client.getPlayer());
      }
      DataBase.getInstance().init().query(FactionCache.factions.values()).update();
    }
}
