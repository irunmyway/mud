package com.eztv.mud;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.bean.task.TaskAction;
import com.eztv.mud.bean.task.TaskCondition;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.utils.BProp;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.colorString;
import static com.eztv.mud.GameUtil.getAttribute;
import static com.eztv.mud.handler.DataHandler.getBaseAttribute;

public class LuaUtil {

    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-07-25 12:19
     * 功能: 通信·部分
     **/
    public void 发送消息(Client client,byte[] msg){
        client.sendMsg(msg);
    }

    ////////////任务部分//////////////////////////////////////////////////////////////
    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-07-25 9:47
     * 功能: 任务部分
     **/
    private Task task = new Task();
    private List<TaskAction> taskActions = new ArrayList<>();
    public LuaUtil taskCreate(String id, Enum.taskState taskState, String nextId, String desc) {//任务状态
        taskActions.clear();
        task.setId(id);
        task.setTaskState(taskState);
        task.setNextId(nextId);
        task.setDesc(desc);
        return this;
    }

    public LuaUtil taskSetReward(Bag bag) {//任务奖励
        task.setReward(bag);
        return this;
    }

    public LuaUtil taskCreateAction(String id, int num) {//添加具体任务
        TaskAction taskAction = new TaskAction();
        taskAction.setId(id);
        taskAction.setNum(num);
        taskActions.add(taskAction);
        return this;
    }

    public LuaUtil taskAddCondition(Enum.taskType taskType) {//添加一个任务任务类型
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setType(taskType);
        taskCondition.setTaskActions(taskActions);
        task.getTaskConditions().add(taskCondition);
        //taskActions.clear();
        return this;
    }

    public void 发送奖励(Client client, Bag reward) {
        DataHandler.sendReward(client, client.getPlayer().getPlayerData().toReward(reward));
    }

    public Task 检查任务状态(Client client,Task task){
        int pos =client.getPlayer().getPlayerData().getTasks().indexOf(task);
        if(pos==-1){//尚未接受该任务
            task.setTaskState(Enum.taskState.processing);
            client.getPlayer().getPlayerData().getTasks().add(task);//添加该任务
        }else{//已经接受
            Task mTask = client.getPlayer().getPlayerData().getTasks().get(pos);
            return mTask;
        }
        return task;
    }

    public void setReward(Bag reward) {
        this.task.setReward(reward);
    }

    public Task getTask() {
        return task;
    }

    ////////////////////////////////////////创建选项部分/////////////////////////////////////////
    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-07-25 9:46
     * 能: 创建选项部分/
     **/

    private List<Choice> choice = new ArrayList<>();
    public void 添加选项(String name,Enum.messageType type, String cmd, String msg, String key, Enum.winAction winAction){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        c.setType(type);
        c.setKey(key);
        c.setAction(winAction);
        choice.add(c);
    }
    public void 添加选项(String name,Enum.messageType type, String cmd, String msg, String key){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        c.setType(type);
        c.setKey(key);
        choice.add(c);
    }
    public List<Choice> getChoice() {
        return choice;
    }



    ///////////////////////////////////////玩家自身部分//////////////////////////////////////////

    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-07-25 9:47
     *功能: 玩家自身部分
     **/
    public int getLevel(Client client) {
        return client.getPlayer().getLevel();
    }

    public String doEquip(Client client, Item item) {//装备上当前物品
        String str = "";
        Equip equip = client.getPlayer().getPlayerData().getEquip();
        synchronized (this) {
            switch (item.getEquipType()) {
                case shoes:
                    if (equip.getShoes() != null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getShoes().getId(), 1);
                    equip.setShoes(null);//卸掉身上的装备回背包
                    equip.setShoes(item);
                    break;
                case cloth:
                    if (equip.getCloth() != null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getCloth().getId(), 1);
                    equip.setCloth(null);//卸掉身上的装备回背包
                    equip.setCloth(item);
                    break;
                case head:
                    if (equip.getHead() != null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getHead().getId(), 1);
                    equip.setHead(null);//卸掉身上的装备回背包
                    equip.setHead(item);
                    break;
                case pants:
                    if (equip.getPants() != null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getPants().getId(), 1);
                    equip.setPants(null);//卸掉身上的装备回背包
                    equip.setPants(item);
                    break;
                case weapon:
                    if (equip.getWeapon() != null)
                        client.getPlayer().getPlayerData().getBag().addItem(equip.getWeapon().getId(), 1);
                    equip.setWeapon(null);//卸掉身上的装备回背包
                    equip.setWeapon(item);
                    break;
            }
            //移除背包的
            client.getPlayer().getPlayerData().getBag().delItem(item.getId(), 1);
            getAttribute(client);
        }

        //开始装备对比
        Attribute cur = getBaseAttribute(client.getPlayer().getLevel());//当前的；
        str += colorString(String.format(BProp.getInstance().getProp().get("equip_variable_show").toString(),
                cur.getAtk(), item.getAttribute().getAtk(),
                cur.getHp_max(), item.getAttribute().getHp_max()
        ));
        //基础的，加装备叠加的，加临时的
        client.getPlayer().onAttributeChange();
        return str;
    }

}
