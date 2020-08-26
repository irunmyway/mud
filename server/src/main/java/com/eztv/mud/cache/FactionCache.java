package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.bean.Room;
import com.eztv.mud.utils.BProp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.DEFAULT_FACTION_MAP_ID;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-23 15:28
 * 功能: 门派缓存类
 **/
public class FactionCache {
    public static HashMap<String, Faction> factions =new HashMap<>();
    public static List<String> grantAlias=new ArrayList<>();
    public static void initFactionCache(){
        grantAlias.clear();
        factions.clear();
        List<Faction> factionList = DataBase.getInstance().init().createSQL("select * from t_faction").list(Faction.class);
        for(Faction faction :factionList){
            faction.setAlias();
            bindFactionRoom(faction);
            factions.put(faction.getId()+"",faction);
        }
        factionList=null;
        for(int i = 1;i<7 ;i++){
            grantAlias.add(BProp.getInstance().get("faction","faction_grant"+i));
        }
    }
    
    public static synchronized void remove(String id){
        factions.remove(id);
    }

    //绑定帮派地图 默认帮派地图的id为1
    public static void bindFactionRoom(Faction faction){
        //得到帮派地图
        HashMap<String,Room> map = (HashMap<String, Room>) RoomCache.maps.get(DEFAULT_FACTION_MAP_ID).clone();
        Collection<Room> rooms =  map.values();
        //修改map为帮派id的hashcode
        rooms.forEach(room -> {
            room.setMap(faction.getId().hashCode());
        });
        RoomCache.addMap(rooms);
    }
}
