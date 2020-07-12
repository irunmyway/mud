package com.eztv.mud.bean.net;

import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.handler.core.Battle;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BObject;
import online.sanen.cdm.template.jpa.Column;
import online.sanen.cdm.template.jpa.Id;
import online.sanen.cdm.template.jpa.NoDB;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.sql.Timestamp;
import java.util.Date;

public class Player extends GameObject {
    private String key ;
    @Column
    private String name;
    private int level;
    private Enum.sex sex;
    private String profession;//职业
    private String desc;//

    //普通状态
    private Enum.playerState playerState;

    //存储的数据
    @Column()
    private String data;

    //属性
    @Column()
    private Date createat;
    private Date updateat;

    //发动攻击
    private Battle battle = new Battle();

    @JSONField(serialize = false)
    private PlayerData playerData;//后端存储信息用的不传输



    public Player() {
    }

    public Player(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Client client, GameObject who, GameObject target, String targetKey) {
        if(BObject.isNotEmpty(this.battle))this.battle.destroy();
        this.battle.init(client,who,target,targetKey);
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

    public Attribute getAttribute() {
        return getPlayerData().getAttribute();
    }

    public void setAttribute(Attribute attribute) {
        getPlayerData().setAttribute(attribute);
    }

    public SendGameObject toSendGameObject(){
        SendGameObject obj = new SendGameObject();
        obj.setKey(key);
        obj.setName(name);
        obj.setObjType(Enum.gameObjectType.player);
        obj.setPlayerData(this.playerData);
        return obj;
    }

}
