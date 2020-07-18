package com.eztv.mud;

import com.eztv.mud.bean.Bag;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.bean.task.TaskAction;
import com.eztv.mud.bean.task.TaskCondition;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

public class LuaUtil {
    private Task task = new Task();
    private List<TaskAction> taskActions = new ArrayList<>();
    public LuaUtil taskCreate(String id, Enum.taskState taskState, String nextId,String desc){//任务状态
        taskActions.clear();
        task.setId(id);
        task.setTaskState(taskState);
        task.setNextId(nextId);
        task.setDesc(desc);
        return this;
    }
    public LuaUtil taskSetReward(Bag bag){//任务奖励
        task.setReward(bag);
        return this;
    }
    public LuaUtil taskCreateAction(String id,int num) {//添加具体任务
        TaskAction taskAction = new TaskAction();
        taskAction.setId(id);
        taskAction.setNum(num);
        taskActions.add(taskAction);
        return this;
    }
    public LuaUtil taskAddCondition(Enum.taskType taskType){//添加一个任务任务类型
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setType(taskType);
        taskCondition.setTaskActions(taskActions);
        task.getTaskConditions().add(taskCondition);
        //taskActions.clear();
        return this;
    }
    public void senReward(Client client,Bag reward){
        DataHandler.sendReward(client, client.getPlayer().getPlayerData().toReward(reward));
    }
    public void setReward(Bag reward) {
        this.task.setReward(reward);
    }

    public Task getTask() {
        return task;
    }

    ////////////////////////////////////////创建选项部分/////////////////////////////////////////
    private List<Choice> choice = new ArrayList<>();
    public List<Choice> getChoice() {
        return choice;
    }
    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }

    ///////////////////////////////////////玩家自身部分//////////////////////////////////////////
    public int getLevel(Client client){
        return client.getPlayer().getLevel();
    }
    public void doEquip(Client client, Item item){//装备上当前物品
        switch (item.getEquipType()){
            case shoes:
                client.getPlayer().getPlayerData().getEquip().setShoes(null);//卸掉身上的装备回背包
                client.getPlayer().getPlayerData().getEquip().setShoes(item);
                break;
            case cloth:
                client.getPlayer().getPlayerData().getEquip().setCloth(null);//卸掉身上的装备回背包
                client.getPlayer().getPlayerData().getEquip().setCloth(item);
                break;
            case head:
                client.getPlayer().getPlayerData().getEquip().setHead(null);//卸掉身上的装备回背包
                client.getPlayer().getPlayerData().getEquip().setHead(item);
                break;
            case pants:
                client.getPlayer().getPlayerData().getEquip().setPants(null);//卸掉身上的装备回背包
                client.getPlayer().getPlayerData().getEquip().setPants(item);
                break;
            case weapon:
                client.getPlayer().getPlayerData().getEquip().setWeapon(null);//卸掉身上的装备回背包
                client.getPlayer().getPlayerData().getEquip().setWeapon(item);
                break;
        }

    }
}
