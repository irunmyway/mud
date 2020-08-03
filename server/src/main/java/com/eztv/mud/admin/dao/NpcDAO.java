package com.eztv.mud.admin.dao;


public interface NpcDAO {
    String getNpcList(int page,int limit);
    String getNpcListByWhere(String value,int page,int limit);
    boolean delNpc(String id);
    boolean createNpc();
    boolean saveNpc(String id,String name,String desc,String map ,String script,String num);
}
