package com.eztv.mud.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.GameUtil;
import com.eztv.mud.PropertiesUtil;
import com.eztv.mud.Word;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.SendGameObject;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.bean.task.TaskAction;
import com.eztv.mud.bean.task.TaskCondition;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BObject;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.eztv.mud.Constant.clients;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.doAttack;
import static com.eztv.mud.constant.Cmd.onObjectOutRoom;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-02 20:40
 * 功能: 游戏物品人物等等所有元素的基类
 **/
public abstract class GameObject {
    private int id;//数据库标识
    private String key;//唯一标识
    private String name;
    private String objectName;
    @JSONField(serialize = false)
    private int map;//当前地图
    @JSONField(serialize = false)
    private long refreshment;//重新产出
    private Attribute attribute=new Attribute();//属性
    private String desc;//实体介绍说明
    private com.eztv.mud.bean.callback.IGameObject iGameObject;//死亡监听
    @JSONField(serialize = false)
    private String script;//绑定的游戏脚本

    //攻击命令
    public GameObject Attack(GameObject gameObject, Client client) {
        AttackPack ap = new AttackPack();
        Properties dbConfig = PropertiesUtil.getInstance().getProp();
        ap.setDesc(GameUtil.colorString(String.format(dbConfig.get("fight_hit").toString(),this.getAttribute().getAck())));
        ap.setWho(this.getKey());
        if (gameObject == null) return null;
        ap.setTarget(gameObject.getKey());
        Attribute attribute;
        attribute = (gameObject instanceof Player) ? ((Player) gameObject).getPlayerData().getAttribute() : gameObject.getAttribute();
        ap.setTargetAttribute(attribute);
        for (Client item : clients) {
            if (BObject.isNotEmpty(item.getPlayer().getPlayerData().getRoom()) && BObject.isNotEmpty(client.getPlayer().getPlayerData().getRoom()))
                if (item.getPlayer().getPlayerData().getRoom().equals(client.getPlayer().getPlayerData().getRoom()))
                    item.sendMsg(msgBuild(Enum.messageType.action, doAttack, object2JsonStr(ap), client.getRole()).getBytes());
        }
        if (gameObject != null)
            if ((gameObject.getAttribute().Attack(this.getAttribute().getAck()) < 1)) {
                onDied(this, gameObject, client);
                return null;
            }
        return gameObject;
    }

    public void onDied(GameObject whoKill, GameObject diedObj, Client client) {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        //死亡回调
        if (client.getPlayer().equals(diedObj)) {//主角死亡
            choice.add(Choice.createChoice("复活", Enum.messageType.action, "relive", null, null));
            winMsg.setChoice(choice);
            winMsg.setDesc("您已经死亡</p><br>&emsp;" + "请选择如何转生。");
            client.sendMsg(msgBuild(Enum.messageType.unHandPop, "relive", object2JsonStr(winMsg), null).getBytes());
        } else {

            /**
             作者：hhx QQ1025334900
             日期: 2020-07-15 17:27
             用处：任务触发 查看玩家任务 并且计数
             **/

            for (Task task : client.getPlayer().getPlayerData().getTasks()) {
                if (task.getTaskState() == Enum.taskState.processing) {
                    task.setTaskState(Enum.taskState.finished);
                    for (TaskCondition taskCondition : task.getTaskConditions()) {
                        for (TaskAction taskAction : taskCondition.getTaskActions()) {
                            if (taskAction.getId().equals(diedObj.getId() + "")) {
                                if(taskAction.addProcess(1)<taskAction.getNum())
                                    task.setTaskState(Enum.taskState.processing);
                            }
                        }
                    }
                }
            }

            /**
             作者：hhx QQ1025334900
             日期: 2020-07-15 17:26
             用处：//移除玩家杀死的其他东西   奖励触发
             **/
            client.getScriptExecutor().loadfile(diedObj.getScript() + ".lua").call();
            Bag reward = JSONObject.toJavaObject(jsonStr2Json(client.getScriptExecutor().get(LuaValue.valueOf("reward")).invoke().toString()), Bag.class);
            DataHandler.sendReward(client, client.getPlayer().getPlayerData().toReward(reward));
            for (Client item : clients) {
                try {
                    Word.getInstance().getRooms().get(((Player) whoKill).getPlayerData().getRoom()).remove(diedObj);
                    item.sendMsg(msgBuild(Enum.messageType.normal, onObjectOutRoom, object2JsonStr(diedObj), null));
                } catch (Exception e) {
                }
            }

            /**
             作者：hhx QQ1025334900
             日期: 2020-07-15 17:26
             用处：怪物刷新模块触发
             **/
            if (diedObj.getRefreshment() == 0) return;
            diedObj.iGameObject.onRefresh(client);
        }


    }

    public Attribute getAttribute() {
        if (this instanceof Player)
            return ((Player) this).getPlayerData().getAttribute();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return colorString(name);
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

    public SendGameObject toSendGameObject() {
        SendGameObject obj = new SendGameObject();
        obj.setKey(getKey());
        obj.setName(getName());
        obj.setAttribute(attribute);
        if (this instanceof Npc)
            obj.setObjType(Enum.gameObjectType.npc);
        if (this instanceof Monster)
            obj.setObjType(Enum.gameObjectType.monster);
        if (this instanceof Player)
            obj.setObjType(Enum.gameObjectType.player);
        return obj;
    }
}


