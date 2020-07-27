package com.eztv.mud;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.bean.task.TaskAction;
import com.eztv.mud.bean.task.TaskCondition;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.script.LuaOpen;
import com.eztv.mud.utils.BProp;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.handler.DataHandler.getBaseAttribute;

public class LuaUtil implements LuaOpen.LuaAction {

    public void 发送消息(Client client,byte[] msg){
        client.sendMsg(msg);
    }
    @Override
    public void 返回数组消息(Client client, String messageType, String cmd, String key, Object obj) {
        GameUtil.sendToSelf(client,msgBuild(Enum.messageType.valueOf(messageType), cmd,objectArr2JsonStr(obj),key));
    }

    @Override
    public void 返回元素消息(Client client, String messageType, String cmd, String key, Object obj) {
        GameUtil.sendToSelf(client,msgBuild(Enum.messageType.valueOf(messageType), cmd,object2JsonStr(obj),key));
    }


    ////////////任务部分//////////////////////////////////////////////////////////////
    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-07-25 9:47
     * 功能: 任务部分
     **/

    List<TaskAction> taskActions = new ArrayList<>();
    List<Choice> choice = new ArrayList<>();
    Task task = new Task();
    public LuaUtil 创建任务(String id, Enum.taskState taskState, String nextId, String desc) {//任务状态
        taskActions.clear();
        task.setId(id);
        task.setTaskState(taskState);
        task.setNextId(nextId);
        task.setDesc(desc);
        return this;
    }
    @Override
    public Task 取任务() {
        return task;
    }
    public LuaUtil 创建任务条件(String id, int num) {//添加具体任务
        TaskAction taskAction = new TaskAction();
        taskAction.setId(id);
        taskAction.setNum(num);
        taskActions.add(taskAction);
        return this;
    }

    public LuaUtil 添加条件到任务(String taskType) {//添加一个任务任务类型
        TaskCondition taskCondition = new TaskCondition();
        taskCondition.setType(Enum.taskType.valueOf(taskType));
        taskCondition.setTaskActions(taskActions);
        task.getTaskConditions().add(taskCondition);
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
            return task;
        }else{//已经接受
            Task mTask = client.getPlayer().getPlayerData().getTasks().get(pos);
            return mTask;
        }
    }

    public void 设置奖励(Bag reward) {
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

    public void 添加选项(String name,String type, String cmd, String msg, String key, String winAction){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        c.setType(Enum.messageType.valueOf(type));
        c.setKey(key);
        c.setAction(Enum.winAction.valueOf(winAction));
        choice.add(c);
    }
    public void 添加选项(String name,String type, String cmd, String msg, String key){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        c.setType(Enum.messageType.valueOf(type));
        c.setKey(key);
        choice.add(c);
    }
    public void 添加选项( Choice c){
        choice.add(c);
    }
    public void 添加执行选项(String name, String cmd, String msg, String key,String winAction){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        c.setType(Enum.messageType.action);
        c.setKey(key);
        c.setAction(Enum.winAction.valueOf(winAction));
        choice.add(c);
    }
    public void 添加面板选项(String name, String cmd, String msg, String key,String winAction){
        Choice c = new Choice();
        c.setName(name);
        c.setCmd(cmd);
        c.setMsg(msg);
        c.setType(Enum.messageType.pop);
        c.setKey(key);
        c.setAction(Enum.winAction.valueOf(winAction));
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
    public int 当前等级(Client client) {
        return client.getPlayer().getLevel();
    }

    public String 装备(Client client, Item item) {//装备上当前物品
        String str = "";
        Equip equip = client.getPlayer().getPlayerData().getEquip();
        synchronized (this) {
            switch (item.getEquipType()) {
                case shoes:
                    if (equip.getShoes() != null)
                        client.getPlayer().getPlayerData().getBag().giveItem(equip.getShoes().getId(), 1);
                    equip.setShoes(null);//卸掉身上的装备回背包
                    equip.setShoes(item);
                    break;
                case cloth:
                    if (equip.getCloth() != null)
                        client.getPlayer().getPlayerData().getBag().giveItem(equip.getCloth().getId(), 1);
                    equip.setCloth(null);//卸掉身上的装备回背包
                    equip.setCloth(item);
                    break;
                case head:
                    if (equip.getHead() != null)
                        client.getPlayer().getPlayerData().getBag().giveItem(equip.getHead().getId(), 1);
                    equip.setHead(null);//卸掉身上的装备回背包
                    equip.setHead(item);
                    break;
                case pants:
                    if (equip.getPants() != null)
                        client.getPlayer().getPlayerData().getBag().giveItem(equip.getPants().getId(), 1);
                    equip.setPants(null);//卸掉身上的装备回背包
                    equip.setPants(item);
                    break;
                case weapon:
                    if (equip.getWeapon() != null)
                        client.getPlayer().getPlayerData().getBag().giveItem(equip.getWeapon().getId(), 1);
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
    public void 装配技能(Client client, Item item) {
        if(item!=null)
        client.getPlayer().getPlayerData().getSkill().setCurSkill(item);
    }
    public boolean 学习技能(Client client, Item item) {
        if(item!=null){
            if(!(client.getPlayer().getPlayerData().getSkill().getSkills().contains(item))){
                client.getPlayer().getPlayerData().getSkill().getSkills().add(item);
                client.getPlayer().getPlayerData().getBag().delItem(item.getId(),1);
                return true;
            }
        }
        return false;
    }

    public void 自己系统消息(Client client, String  str){
        Chat chat = new Chat();
        chat.setContent(str);
        chat.setMsgType(Enum.chat.系统);
        GameUtil.sendToSelf(client,msgBuild(Enum.messageType.chat, Enum.chat.公聊.toString(),object2JsonStr(chat),""));
    }


}
