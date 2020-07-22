package com.eztv.mud.bean;

import online.sanen.cdm.template.jpa.Id;
import online.sanen.cdm.template.jpa.Table;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-21 22:11
 用处：帮派
**/
@Table(name = "t_faction")
public class Faction {
    @Id
    private int id;
    private String name;
    private String desc;
    private int level;


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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
