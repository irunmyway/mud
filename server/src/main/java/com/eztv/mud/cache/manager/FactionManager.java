package com.eztv.mud.cache.manager;

import com.eztv.mud.Constant;
import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.FactionCache;

import static com.eztv.mud.cache.manager.ClientManager.getClient;

public class FactionManager {
    //获取帮派
    public static Faction getFaction(Client client){
        return FactionCache.factions.get(client.getPlayer().getFaction()+"");
    }
    public static Faction getFaction(Object factionId){
        if (factionId instanceof Integer)
            return FactionCache.factions.get(factionId+"");
        if (factionId instanceof String)
            return FactionCache.factions.get(factionId);
        return null;
    }
    //加入帮派
    public static synchronized Faction joinFaction(Client client,String factionId){
        Faction faction = getFaction(factionId);//要加入的帮派
        if(faction!=null){
            if(faction.getAllowJoin().get(client.getPlayer().getKey())!=null) {
                client.getPlayer().setFaction(faction.getId());
                client.getPlayer().setFaction_position(0);
                faction.getAllowJoin().remove(client.getPlayer().getKey());
                return faction;
            }
        }
        return null;
    }
    //加入帮派
    public static void joinFaction(int playerKey,int factionId){
        Client client =  getClient(playerKey);//要加入的玩家
        Faction faction = getFaction(factionId);//要加入的帮派
        if(faction!=null&&client!=null){
            client.getPlayer().setFaction(faction.getId());
            client.getPlayer().setFaction_position(0);
        }
    }
    //获取职位别名
    public static String getPositionAlias(int pos){
        String alias="";
        switch (pos){
            case 0:
                alias = FactionCache.grantAlias.get(0);
                break;
            default:
                alias = FactionCache.grantAlias.get(pos-1);
                break;
        }
        return alias;
    }

    //设置职位
    public static boolean setPosition(Player player,int pos){
        for (Client client: Constant.clients){
            if(client.getPlayer().getAccount().equals(player.getAccount())){
                client.getPlayer().setFaction_position(pos);
            }
        }
        //数据库也更新下
        int flag = DataBase.getInstance().init().createSQL("update role set faction_position=? where account =?",
                pos,player.getAccount()).update();
        return flag>0;
    }
}
