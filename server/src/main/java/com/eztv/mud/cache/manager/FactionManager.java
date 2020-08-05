package com.eztv.mud.cache.manager;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.utils.BProp;

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
                alias = BProp.getInstance().get("faction","faction_grant1");
                break;
            case 1:
                alias = BProp.getInstance().get("faction","faction_grant1");
                break;
            case 2:
                alias = BProp.getInstance().get("faction","faction_grant2");
                break;
            case 3:
                alias = BProp.getInstance().get("faction","faction_grant3");
                break;
            case 4:
                alias = BProp.getInstance().get("faction","faction_grant4");
                break;
            case 5:
                alias = BProp.getInstance().get("faction","faction_grant5");
                break;
        }
        return alias;
    }
}
