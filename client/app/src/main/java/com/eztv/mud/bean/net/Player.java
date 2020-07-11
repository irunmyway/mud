package com.eztv.mud.bean.net;

import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.PlayerData;
import java.sql.Timestamp;
import java.util.Date;

public class Player extends GameObject {
    private String key;
    private String name;
    private int level;
    private Enum.sex sex;
    private String profession;//职业
    private String desc;//

    //普通状态
    private Enum.playerState playerState;
    private Attribute attribute = new Attribute();

    //存储的数据
    private String data;

    //属性
    private Date createat;
    private Date updateat;


    private PlayerData playerData;

    public Player() {
    }

    public Player(String name, int level) {
        this.name = name;
        this.level = level;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Enum.sex getSex() {
        return sex;
    }

    public void setSex(Enum.sex sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public PlayerData getPlayerData() {
        if(playerData==null){
            playerData = new PlayerData();
        }
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    @Override
    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
