package com.eztv.mud.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-25 16:55
 * 功能: 玩家技能
 **/
public class Skills {
    private Skill curSkill;//当前出招技能

    private List<Skill> skills = new ArrayList<>();//玩家所有技能

    public void delSkill(int id, int num) {
        if (num < 1) num = 1;
        Item delItem = null;
        for (Item item : skills) {
            if (item.getId() == id) {
                if (item.getNum() > 1) {
                    if (item.getNum() - num < 1) {
                        delItem = item;
                    } else {
                        item.setNum(item.getNum() - num);
                    }
                } else {
                    delItem = item;
                }
            }
        }
        if (delItem != null)
            skills.remove(delItem);
    }

    public Skill getCurSkill() {
        return curSkill;
    }

    public void setCurSkill(Skill curSkill) {
        this.curSkill = null;
        this.curSkill = curSkill;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Attribute calculate(){
        if(curSkill==null)return new Attribute();
        return  curSkill.getAttribute();
    }
}
