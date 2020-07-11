package com.eztv.mud.bean;

import com.eztv.mud.Word;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.bean.Cmd.doAttack;
import static com.eztv.mud.bean.Cmd.onObjectOutRoom;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-02 20:40
 * 功能: 游戏物品人物等等所有元素的基类
 **/
public abstract class GameObject{
    private String key;//唯一标识
    private String name;
    private  String objectName;
    private int map;//当前地图
    private long refreshment;//重新产出
    private Attribute attribute;//属性
    private String desc;//实体介绍说明
    private com.eztv.mud.bean.callback.IGameObject iGameObject;//死亡监听
    private String script;//绑定的游戏脚本
    //攻击命令
    public GameObject Attack(GameObject gameObject,Client client) {
        AttackPack ap = new AttackPack();
        if(gameObject!=null)
        if((gameObject.getAttribute().Attack(this.getAttribute().getAck())<1)){
            onDied(this,gameObject,client);
            return null;
        }
        ap.setDesc("<font color=\"#ffffff\">造成伤害 -"+this.getAttribute().getAck()+"</font>");
        ap.setWho(this.getKey());
        if(gameObject==null)return null;
        ap.setTarget(gameObject.getKey());
        Attribute attribute;
        attribute= (gameObject instanceof Player)?((Player)gameObject).getPlayerData().getAttribute():gameObject.getAttribute();
        ap.setTargetAttribute(attribute);
        for (Client item:clients){
                if(BObject.isNotEmpty(item.getPlayer().getPlayerData().getRoom())&&BObject.isNotEmpty(client.getPlayer().getPlayerData().getRoom()))
                if(item.getPlayer().getPlayerData().getRoom().equals(client.getPlayer().getPlayerData().getRoom()))
                    item.sendMsg(msgBuild(Enum.messageType.action, doAttack,object2JsonStr(ap),client.getRole()).getBytes());
           }
        return gameObject;
    }

    public void onDied(GameObject whoKill, GameObject diedObj,Client client) {
        //死亡回调
        if(client.getPlayer().equals(diedObj)){//主角死亡
                BDebug.trace("测试"+"主角死亡");
        }else{//移除玩家杀死的其他东西
            for (Client item:clients){
                try {
                    Word.getInstance().getRooms().get(((Player)whoKill).getPlayerData().getRoom()).remove(diedObj);
                    item.sendMsg(msgBuild(Enum.messageType.normal,onObjectOutRoom,object2JsonStr(diedObj),null));
                }catch (Exception e){}
            }

            if(diedObj.getRefreshment()==0)return;
            diedObj.iGameObject.onRefresh(client);
        }


    }

    public Attribute getAttribute() {
        if(this instanceof Player)
            return ((Player)this).getPlayerData().getAttribute();
        return attribute;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public long getRefreshment() {
        return refreshment;
    }

    public void setRefreshment(long refreshment) {
        this.refreshment = refreshment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }




    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }


    public void setiGameObject(com.eztv.mud.bean.callback.IGameObject iGameObject) {
        this.iGameObject = iGameObject;
    }

    public int getMap() {
        return map;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}


