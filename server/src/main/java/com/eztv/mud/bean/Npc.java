package com.eztv.mud.bean;

import com.eztv.mud.utils.BDebug;

public class Npc extends GameObject implements Cloneable{
    private int id;
    private String name;
    private String desc;
    private int map;
    private String script;
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }


    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }



    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
