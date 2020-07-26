package com.eztv.mud.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.constant.Enum;

import static com.eztv.mud.GameUtil.getProp;


public class Item extends GameObject implements Cloneable{
    private int id;
    private String script;
    @JSONField(serialize = false)
    private Attribute attribute;
    private int num;
    private Enum.itemType type;
    private Enum.equipType equipType;
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Enum.itemType getType() {
        return type;
    }

    public void setType(Enum.itemType type) {
        this.type = type;
    }
    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String toDesc(Enum.itemType type){
        String str="";
        str+=getName()+"</p><br>";
        switch (type){
            case equip:
                str+= getProp("equip_detail_hit",getAttribute().getAtk());
                break;
        }
        return str;
    }

    public Enum.equipType getEquipType() {
        return equipType;
    }

    public void setEquipType(Enum.equipType equipType) {
        this.equipType = equipType;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false ;
        }
        if (obj instanceof Item){
            Item item = (Item) obj;
            if(item.getId() == this.id){
                return true ;
            }
        }
        return false ;
    }
}
