package com.eztv.mud.admin.dao;


public interface SkillDAO {
    String getSkillList(int page, int limit);
    String getSkillListByWhere(String value,int page, int limit);
    boolean delSkill(String id);
    boolean createSkill();
    boolean saveSkill(String id,String name,String script);
}
