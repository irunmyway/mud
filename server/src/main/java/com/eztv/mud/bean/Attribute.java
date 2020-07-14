package com.eztv.mud.bean;

import java.util.HashMap;

public class Attribute implements Cloneable{
    private int level;

    private long hp;
    private long hp_max;
    private long mp;
    private long mp_max;
    private long exp;
    private long exp_max;

    private long ack;//攻击力

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public long getHp_max() {
        return hp_max;
    }

    public void setHp_max(long hp_max) {
        this.hp_max = hp_max;
    }

    public long getMp() {
        return mp;
    }

    public void setMp(long mp) {
        this.mp = mp;
    }

    public long getMp_max() {
        return mp_max;
    }

    public void setMp_max(long mp_max) {
        this.mp_max = mp_max;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
    public void addExp(long exp) {
        this.exp += exp;
    }
    public long getExp_max() {
        return exp_max;
    }

    public long getAck() {
        return ack;
    }

    public void setAck(long ack) {
        this.ack = ack;
    }

    public void setExp_max(long exp_max) {
        this.exp_max = exp_max;
    }
    public long Attack(long hp){
        setHp(getHp()-hp);
        return getHp();
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
