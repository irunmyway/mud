package com.eztv.mud.bean.net;

import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.callback.IPlayerCallBack;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.handler.core.Battle;
import com.eztv.mud.handler.core.TaskHandler;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;
import online.sanen.cdm.template.jpa.Column;

import java.util.Date;

public class Player extends GameObject implements IPlayerCallBack {
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

    //任务检测
    private TaskHandler taskHandler = new TaskHandler();

    @JSONField(serialize = false)
    private PlayerData playerData;//后端存储信息用的不传输
    @JSONField(serialize = false)
    private Client client;

    public Player(Client client) {
        this.client = client;
    }

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

    public TaskHandler getTaskHandler() {
        return taskHandler;
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
            playerData = new PlayerData(this);
        }
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public String getProfession() {
        return profession;
    }

    public void setClient(Client client) {
        this.client = client;
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
        obj.setLevel(level);
        obj.setKey(key);
        obj.setName(name);
        obj.setObjType(Enum.gameObjectType.player);
        obj.setPlayerData(this.playerData);
        return obj;
    }

    @Override
    public void onUpLevel() {
        if(getAttribute().getExp()>getAttribute().getExp_max()){
            long tempExp = Math.abs(getAttribute().getExp()-getAttribute().getExp_max() );
            this.level++;
            DataHandler.getPlayerByUpLevel(client,this);
            this.getAttribute().setExp(tempExp);
            //然后发送属性
            GameUtil.getSelf(client);
        }
    }
}