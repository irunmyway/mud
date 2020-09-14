package com.eztv.mud.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.callback.IGameObject;
import com.eztv.mud.bean.net.AttackPack;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.SendGameObject;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.event.player.PlayerDead;
import com.eztv.mud.utils.BProp;
import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.Properties;

import static com.eztv.mud.Constant.Algorithm_Attack;
import static com.eztv.mud.Constant.脚本_事件_战斗事件;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.doAttack;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-02 20:40
 * 功能: 游戏物品人物等等所有元素的基类
 **/
public abstract class GameObject{
    private int id;//数据库标识
    private String key;//唯一标识
    private String name;
    private String objectName;
    @JSONField(serialize = false)
    @Expose(serialize = false, deserialize = false)
    private int map=0;//当前地图
    @JSONField(serialize = false)
    @Expose(serialize = false, deserialize = false)
    private long refreshment;//重新产出
    private Attribute attribute=new Attribute();//属性
    private String desc;//实体介绍说明
    @JSONField(serialize = false)
    private com.eztv.mud.bean.callback.IGameObject iGameObject;//死亡监听
    @JSONField(serialize = false)
    @Expose(serialize = false, deserialize = false)
    private String script;//绑定的游戏脚本
    private Date createat;


    //攻击命令
    public GameObject Attack(GameObject gameObject, Client client) {
//        float realAtc = computeRealAtk(this,gameObject);
//        boolean isAttack = AttackAlgorithm.computeAccuracy(this,gameObject);
        double realAtc =(double) client.getScriptExecutor().load(Algorithm_Attack).
                execute("攻击",
                        this,gameObject
                );
        boolean isAttack = (Boolean) client.getScriptExecutor().load(Algorithm_Attack).
                execute("命中",
                this,gameObject
                );

        AttackPack ap = new AttackPack();
        Properties dbConfig = BProp.getInstance().getProp();
        if(isAttack){ //攻击
            Skill curSkill = client.getPlayer().getPlayerData().getSkill().getCurSkill();
            if (curSkill!=null){
                if(!getAttribute().AttackMp(curSkill.getAttribute().getMp())){//普通攻击
                    client.getPlayer().getPlayerData().getSkill().setCurSkill(new Skill());
                    ap.setDesc(GameUtil.colorString(String.format(dbConfig.get("fight_hit").toString(),(int)realAtc)));
                }else{//释放技能
                    ap.setDesc(GameUtil.colorString(String.format(dbConfig.get("fight_hit_skill").toString(),curSkill.getName(),(int)realAtc)));
                }
            }else{//普通攻击
                ap.setDesc(GameUtil.colorString(String.format(dbConfig.get("fight_hit").toString(),(int)realAtc)));
            }
        }else{//闪避
            ap.setDesc(GameUtil.colorString(dbConfig.get("fight_eva").toString()));
        }

        if (gameObject != null&&isAttack) {//攻击后血量小于1 死亡
            if ((gameObject.getAttribute().Attack((long)realAtc) < 1))
                PlayerDead.onDied(this, gameObject, client);
        }

        if (!(gameObject instanceof Player)) {//战斗事件AI
            client.getScriptExecutor().load(gameObject.getScript());
            client.getScriptExecutor().execute(脚本_事件_战斗事件, client,gameObject, new WinMessage());
        }

        ap.setWho(this.getKey());
        if (gameObject == null) return null;
        ap.setTarget(gameObject.getKey());
        Attribute attribute;
        attribute = (gameObject instanceof Player) ? ((Player) gameObject).getPlayerData().getAttribute() : gameObject.getAttribute();
        ap.setTargetAttribute(attribute);
        //发送房间战斗消息
        GameUtil.sendToRoom(client, msgBuild(Enum.messageType.action, doAttack, object2JsonStr(ap), client.getRole()));
        return gameObject;
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

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
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
        return script==null?"":script;
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


    public IGameObject getiGameObject() {
        return iGameObject;
    }


}


