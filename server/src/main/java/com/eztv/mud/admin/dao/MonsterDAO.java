package com.eztv.mud.admin.dao;


public interface MonsterDAO {
    String getMonsterList(int page,int limit);
    String getMonsterListByWhere(String value,int page,int limit);
    boolean delMonster(String id);
    boolean createMonster();
    boolean saveMonster(String id,String name,String script,int num,long refreshment,int map);
}
