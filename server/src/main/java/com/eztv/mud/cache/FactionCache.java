package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Faction;

import java.util.HashMap;
import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-23 15:28
 * 功能: 门派缓存类
 **/
public class FactionCache {
    public static HashMap<String, Faction> factions=new HashMap<>();

    public static void initFactionCache(){
        factions.clear();
        List<Faction> factionList = DataBase.getInstance().init().createSQL("select * from t_faction").list(Faction.class);
        for(Faction faction :factionList){
            factions.put(faction.getId()+"",faction);
        }
        factionList=null;
    }

    public static synchronized void remove(int id){
        factions.remove(id+"");
    }
}
