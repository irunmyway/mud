package com.eztv.mud.bean;

import com.eztv.mud.GameUtil;
import com.eztv.mud.script.LuaOpen;

public class Attribute implements Cloneable, LuaOpen.LuaJson {
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
    private long luk;//幸运
    private double shp;//吸血suck hp
    private double smp;//吸魔suck mp


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

    public long getLuk() {
        return luk;
    }

    public void setLuk(long luk) {
        this.luk = luk;
    }

    public double getShp() {
        return shp;
    }

    public void setShp(double shp) {
        this.shp = shp;
    }

    public double getSmp() {
        return smp;
    }

    public void setSmp(double smp) {
        this.smp = smp;
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

    public long getAtk() { return atk;}

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
    public boolean AttackMp(long mp){
        if(getMp()>mp){
            setMp(getMp()-mp);
            return true;
        }else{
            return false;
        }
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String 到Json() {
        return GameUtil.object2JsonStr(this);
    }
}
