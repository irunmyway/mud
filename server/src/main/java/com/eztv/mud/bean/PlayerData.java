package com.eztv.mud.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.task.Task;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private String room;
    private Attribute attribute = new Attribute();//玩家的基础属性
    private List<Task> tasks = new ArrayList<>();//玩家的任务集合
    private Bag bag = new Bag();//玩家背包
    private Equip equip = new Equip(); //玩家装备
    @JSONField(serialize = false)
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Equip getEquip() {
        return equip;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }


    public List<String> toReward(Bag reward){//变成奖励
        List<String > list = new ArrayList<>();
        getBag().setMoney(getBag().getMoney()+reward.getMoney());
        getBag().setYbMoney(getBag().getYbMoney()+reward.getYbMoney());
        getBag().setJbMoney(getBag().getJbMoney()+reward.getJbMoney());
        if(reward.getMoney()>0)
            list.add("铜币 +"+reward.getMoney());
        if(reward.getJbMoney()>0)
            list.add("金币 +"+reward.getJbMoney());
        if(reward.getYbMoney()>0)
            list.add("元宝 +"+reward.getYbMoney());
        if(reward.getExp()>0){
            getAttribute().addExp(reward.getExp());
            list.add("得到经验 +"+reward.getExp());
            if(getAttribute().getExp()>getAttribute().getExp_max())player.onUpLevel();//升级触发
        }
        for(Item item:reward.items){
            list.add("获得了 "+item.getName()+" +"+item.getNum());
            int pos = getBag().getItems().indexOf(item);
            if(pos>-1){//叠加
                item.setNum(getBag().getItems().get(pos).getNum()+item.getNum());
                getBag().getItems().set(pos,item);
            }else{//新增
                getBag().getItems().add(item);
            }
        }
        return list;
    }
}

