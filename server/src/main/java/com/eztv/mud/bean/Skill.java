package com.eztv.mud.bean;

import com.eztv.mud.utils.BDebug;

public class Skill extends Item{
    private int level=1;
    private Long[] skillPot= {}; //升级对应的潜能点
    private Long[]  skillHit={}; //伤害
    private Long[]  skillMp={}; //所消耗的mp

    public Skill() {
        try{
            BDebug.trace("技能等级:"+level);
        }catch(Exception e){}
    }

    public void hit(Long[] str){
        skillHit = str;
        if(skillHit.length>=level)
            getAttribute().setAtk(skillHit[level-1]);
    }

    public void pot(Long[] str){
        skillPot = str;
    }

    public void mp(Long[] str){
        skillMp = str;
        if(skillMp.length>=level)
            getAttribute().setMp(skillMp[level-1]);
    }

    public int getLevel() {
        return level;
    }

    public long getPot(){
        if(skillPot.length>=level){
            return skillPot[level];
        }
        return 0;
    }
}
