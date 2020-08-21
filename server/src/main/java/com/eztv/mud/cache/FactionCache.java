package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.utils.BProp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            factions.put(faction.getId()+"",faction);
        }
        factionList=null;
        for(int i = 1;i<7 ;i++){
            grantAlias.add(BProp.getInstance().get("faction","faction_grant"+i));
        }
    }
    
    public static synchronized void remove(int id){
        factions.remove(id+"");
    }
}
