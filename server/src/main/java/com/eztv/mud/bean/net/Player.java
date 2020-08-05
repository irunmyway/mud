package com.eztv.mud.bean.net;

import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.callback.IPlayerCallBack;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataBaseHandler;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.handler.core.Battle;
import com.eztv.mud.utils.BObject;
import online.sanen.cdm.template.jpa.*;

import java.util.Date;

import static com.eztv.mud.handler.DataHandler.getBaseAttribute;
@Table(name = "role")
public class Player extends GameObject implements IPlayerCallBack {
    @JSONField(serialize = false)
    @NoDB
    @Id
    private String account;
    private int level;
    private Enum.sex sex;
    private String profession;//职业
    private int faction;//帮派

    //普通状态
    private Enum.playerState playerState;

    //存储的数据
    @Column()
    @NoUpdate
    private String data;

    //属性
    private Date updateat;

    //发动攻击
    private Battle battle = new Battle();

    private int faction_position;//帮派职位

    //人物数据处理
    private DataBaseHandler dataBaseHandler = new DataBaseHandler();

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
        setName(name);
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public DataBaseHandler getDataBaseHandler() {
        return dataBaseHandler;
    }

    public int getFaction() {
        return faction;
    }


    public void setFaction(int faction) {
        this.faction = faction;
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
        try{
            playerData.setPlayer(this);
        }catch(Exception e){}
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
        obj.setKey(getKey());
        obj.setName(getName());
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

    @Override
    public void onAttributeChange() {
        //计算角色改变后的数据
        Attribute skillAttr = client.getPlayer().getPlayerData().getSkill().calculate();
        Attribute equipAttr = client.getPlayer().getPlayerData().getEquip().calculate();//装备叠加的
        Attribute cur = getBaseAttribute(client.getPlayer().getLevel());//当前的；
        this.getPlayerData().setAttribute(cur.//叠加属性
                add(skillAttr).
                add(equipAttr).
                addTmp(client.getPlayer().getPlayerData().getAttribute()));
    }

    public int getFaction_position() {
        return faction_position;
    }

    //帮主 副帮主 长老 堂主 成员
    public void setFaction_position(int faction_position) {
        this.faction_position = faction_position;
    }
}