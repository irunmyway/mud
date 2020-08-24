package com.eztv.mud.cache.manager;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Relation;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.RelationCache;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelationManager {
    //添加好友
    public static boolean makeFriend(String account, Player target) {
        Relation relation = new Relation();
        relation.setRole(account);
        relation.setToRole(target.getAccount());
        relation.setType(Enum.relation.好友);
        relation.setCreateat(new Date());
        try {
            if (DataBase.getInstance().init().query(relation).insert() > 0) {
                addRelation(relation);
                relation = new Relation();
                relation.setRole(target.getAccount());
                relation.setToRole(account);
                relation.setType(Enum.relation.好友);
                relation.setCreateat(new Date());
                DataBase.getInstance().init().query(relation).insert();
                addRelation(relation);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static void delRelation(String Acc, String targetAcc, Enum.relation type) {
        synchronized (RelationCache.relationMap){
            List<Relation> relations = RelationCache.relationMap.get(Acc);
            List<Relation> preRemove=new ArrayList<>();
            for (Relation relation : relations) {
                if (relation.getRole().equals(Acc) && relation.getToRole().equals(targetAcc) && relation.getType() == type) {
                    preRemove.add(relation);
                    DataBase.getInstance().init().query(relation).delete();
                }
            }
            relations.removeAll(preRemove);
        }
    }

    //添加关系
    public static void addRelation(Relation relation) {
        synchronized (RelationCache.relationMap){
            List<Relation> relations = RelationCache.relationMap.get(relation.getRole() + "");
            if (relations == null) {
                relations = new ArrayList<>();
                relations.add(relation);
                RelationCache.relationMap.put(relation.getRole() + "", relations);
            } else {
                relations.add(relation);
            }
        }
    }

}
