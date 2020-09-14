package com.eztv.mud.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.task.Task;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
//    private String room;
    @JSONField(serialize = false)
    @Expose(serialize = false, deserialize = false)
    private Room room;
    private Attribute attribute = new Attribute();//玩家的基础属性
    private List<Task> tasks;//玩家的任务集合
    private Bag bag = new Bag();//玩家背包
    private Equip equip = new Equip(); //玩家装备
    private Skills skill = new Skills(); //玩家技能
    @JSONField(serialize = false)
    @Expose(serialize = false, deserialize = false)
    private Player player;
    public PlayerData(Player player) {
        this.player = player;
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Equip getEquip() {
        return equip;
    }

    public List<Task> getTasks() {
        if(tasks==null)tasks = new ArrayList<>();
        return tasks;
    }

    public Skills getSkill() {
        return skill;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }


    public List<String> toReward(Bag reward){//变成奖励
        List<String > list = new ArrayList<>();
        getBag().setMoney(getBag().getMoney()+reward.getMoney());
        getBag().setYbMoney(getBag().getYbMoney()+reward.getYbMoney());
        getBag().setJbMoney(getBag().getJbMoney()+reward.getJbMoney());
        if(reward.getMoney()!=0)
            list.add(GameUtil.getPropByFile("bag","bag_money")+
                    (reward.getMoney()>0?"+":"")+
                    reward.getMoney()+"</font>");
        if(reward.getJbMoney()!=0)
            list.add(GameUtil.getPropByFile("bag","bag_jb")+
                    (reward.getJbMoney()>0?"+":"")+
                    reward.getJbMoney()+"</font>");
        if(reward.getYbMoney()!=0)
            list.add(GameUtil.getPropByFile("bag","bag_yb")+
                    (reward.getYbMoney()>0?"+":"")+
                    reward.getYbMoney()+"</font>");
        if(reward.getExp()>0){
            getAttribute().addExp(reward.getExp());
            list.add("<font color=\"#00BFFF\">得到经验 +"+reward.getExp()+"</font>");
            if(getAttribute().getExp()>getAttribute().getExp_max())player.onUpLevel();//升级触发
        }
        for(Item item:reward.items){
            list.add("获得了 "+item.getName()+" "+(item.getNum()>0?("+"+item.getNum()):item.getNum()));
            int pos = getBag().getItems().indexOf(item);
            if(pos>-1){//叠加
                item.setNum(getBag().getItems().get(pos).getNum()+item.getNum());
                if(item.getNum()>0){
                    getBag().getItems().set(pos,item);
                }else{
                    getBag().getItems().remove(pos);
                }

            }else{//新增
                getBag().getItems().add(item);
            }
        }
        return list;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setEquip(Equip equip) {
        this.equip = equip;
    }

    public void setSkill(Skills skill) {
        this.skill = skill;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String toJson(){
       return GameUtil.object2JsonStr(this);
    }
}

