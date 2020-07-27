package com.eztv.mud.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-25 16:55
 * 功能: 玩家技能
 **/
public class Skill {
    private Item curSkill;//当前出招技能
    private List<Item> skills = new ArrayList<>();//玩家所有技能

    public Skill() {
    }


    public Item getCurSkill() {
        return curSkill;
    }

    public void setCurSkill(Item curSkill) {
        this.curSkill = curSkill;
    }

    public List<Item> getSkills() {
        return skills;
    }

    public void setSkills(List<Item> skills) {
        this.skills = skills;
    }

    public Attribute calculate(){
        if(curSkill==null)return new Attribute();
        return  curSkill.getAttribute();
    }
}
