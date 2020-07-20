package com.eztv.mud.bean;

import online.sanen.cdm.template.jpa.Column;
import online.sanen.cdm.template.jpa.NoDB;
import online.sanen.cdm.template.jpa.Table;

import java.util.HashMap;

public class Attribute implements Cloneable{
    private int level;
    private long hp;
    private long hp_max;
    private long mp;
    private long mp_max;
    private long exp;
    private long exp_max;

    private long atk;//攻击力
    private long def;//防御
    private long acc;//命中
    private long eva;//闪躲

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

    public long getDef() {
        return def;
    }

    public void setDef(long def) {
        this.def = def;
    }

    public long getAcc() {
        return acc;
    }

    public void setAcc(long acc) {
        this.acc = acc;
    }

    public long getEva() {
        return eva;
    }

    public void setEva(long eva) {
        this.eva = eva;
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

    public long getAtk() {
        return atk;
    }

    public void setAtk(long atk) {
        this.atk = atk;
    }

    public Attribute add(Attribute target){//基础属性的相加
        Attribute attribute = new Attribute();
        attribute.setAtk(this.getAtk()+target.getAtk());
        attribute.setHp_max(this.getHp_max()+target.getHp_max());
        attribute.setMp_max(this.getMp_max()+target.getMp_max());
        attribute.setDef(this.getDef()+target.getDef());
        attribute.setAcc(this.getAcc()+target.getAcc());
        attribute.setEva(this.getEva()+target.getEva());
        return attribute;
    }
    public Attribute addTmp(Attribute target){//包含当前属性的相加
        this.setHp(this.getHp()+target.getHp());
        this.setMp(this.getMp()+target.getMp());
        this.setExp(this.getExp()+target.getExp());
        return this;
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
