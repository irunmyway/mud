package com.eztv.mud;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.bean.task.TaskAction;
import com.eztv.mud.bean.task.TaskCondition;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.STR_TITLE;
import static com.eztv.mud.handler.DataHandler.getBaseAttribute;

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
    public String doEquip(Client client, Item item){//装备上当前物品
        String str="";
        Equip equip = client.getPlayer().getPlayerData().getEquip();
        synchronized (this){
            switch (item.getEquipType()){
                case shoes:
                    if(equip.getShoes()!=null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getShoes().getId(),1);
                    equip.setShoes(null);//卸掉身上的装备回背包
                    equip.setShoes(item);
                    break;
                case cloth:
                    if(equip.getCloth()!=null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getCloth().getId(),1);
                    equip.setCloth(null);//卸掉身上的装备回背包
                    equip.setCloth(item);
                    break;
                case head:
                    if(equip.getHead()!=null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getHead().getId(),1);
                    equip.setHead(null);//卸掉身上的装备回背包
                    equip.setHead(item);
                    break;
                case pants:
                    if(equip.getPants()!=null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getPants().getId(),1);
                    equip.setPants(null);//卸掉身上的装备回背包
                    equip.setPants(item);
                    break;
                case weapon:
                    if(equip.getWeapon()!=null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getWeapon().getId(),1);
                    equip.setWeapon(null);//卸掉身上的装备回背包
                    equip.setWeapon(item);
                    break;
            }
            //移除背包的
            client.getPlayer().getPlayerData().getBag().delItem(item.getId(),1);
        }


        //开始装备对比
        //计算角色改变后的数据
        Attribute attribute = client.getPlayer().getPlayerData().getEquip().calculate();//装备叠加的
        Attribute cur = getBaseAttribute(client.getPlayer().getLevel());//当前的；

        str+="属性变化"+"<br>";
        str+="&emsp;攻击力 :"+cur.getAck()+" --> +"+item.getAttribute().getAck()+"<br>";
        str+="&emsp;血上线 :"+cur.getHp_max()+" --> +"+item.getAttribute().getHp_max();

        //基础的，加装备叠加的，加临时的
        client.getPlayer().getPlayerData().setAttribute(cur.add(attribute).addTmp(client.getPlayer().getPlayerData().getAttribute()));
//        client.getPlayer().getAttribute().getAck()
        return str;
    }
}
