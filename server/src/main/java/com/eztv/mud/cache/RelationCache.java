package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Relation;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.manager.RelationManager;
import com.eztv.mud.constant.Enum;

import java.util.HashMap;
import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-22 22:58
 * 功能: 关系缓存
 **/
public class RelationCache {
    public static HashMap<String, List<Relation>> relationMap =new HashMap<>();
    public static HashMap<String, HashMap<String,Boolean>> allows =new HashMap<>();
    public static void initRelationCache(){
        relationMap.clear();
        List<Relation> relationList = DataBase.getInstance().init().createSQL("select * from t_relation").list(Relation.class);
        for(Relation relation :relationList){
            RelationManager.addRelation(relation);
        }
    }

    public static boolean isFriend(Player player, String targetAcc){
        List<Relation> relations = relationMap.get(player.getAccount());
        Relation relation = new Relation();
        relation.setType(Enum.relation.好友);
        relation.setToRole(targetAcc);
        return relations==null?false:relations.contains(relation);
    }

//    public static synchronized void remove(int id){
//        relationMap.remove(id+"");
//    }
}
