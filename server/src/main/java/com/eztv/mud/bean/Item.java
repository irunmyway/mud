package com.eztv.mud.bean;

import com.eztv.mud.constant.Enum;

import static com.eztv.mud.Constant.STR_TITLE;


public class Item extends GameObject implements Cloneable{
    private int id;
    private String name;
    private String script;
    private int num;
    private Enum.itemType type;
    private Enum.equipType equipType;

//    public Item(String name, int num) {
//        this.name = name;
//        this.num = num;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
        str+=name+STR_TITLE;
        switch (type){
            case equip:
                str+="攻击力:"+getAttribute().getAck()+"<br>";
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
