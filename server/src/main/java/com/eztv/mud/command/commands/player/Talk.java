package com.eztv.mud.command.commands.player;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.RelationCache;
import com.eztv.mud.cache.manager.FactionManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.脚本_对话;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.doTalk;

public class Talk extends BaseCommand {
    public Talk(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        GameObject gameObject=getGameObject(getClient(),getMsg().getMsg());
        if(gameObject==null)return;
        List<Choice> choice = new ArrayList<>();
        if(gameObject instanceof Player){//是玩家
            if(gameObject.getAttribute().getHp()>0){//活着的时候
                choice.add(Choice.createChoice("私聊", Enum.messageType.input,"私聊", gameObject.getKey(),null).setBgColor(Enum.color.gray));
                //判断是否是好友
                boolean isFriend= RelationCache. isFriend(getPlayer(),((Player) gameObject).getAccount());
                //不能和自己干的事
                if(!gameObject.getKey().equals(getPlayer().getKey())){
                    if (!isFriend)
                        choice.add(Choice.createChoice("结交", Enum.messageType.action,"makeFriend", gameObject.getKey(),null).setBgColor(Enum.color.yellow));
                    choice.add(Choice.createChoice("攻击", Enum.messageType.action,"attack", gameObject.getKey(),null).setBgColor(Enum.color.red));
                    choice.add(Choice.createChoice("交易", Enum.messageType.action,"trade", gameObject.getKey(),null).setBgColor(Enum.color.yellow));
                    choice.add(Choice.createChoice("组队", Enum.messageType.action,"team", gameObject.getKey(),null).setBgColor(Enum.color.blue));
                }
                //帮派招募
                Faction faction = FactionManager.getFaction(getClient());
                if(faction!=null&&getPlayer().getFaction_position()>0){//管理层可以招募
                    choice.add(Choice.createChoice("招募", Enum.messageType.action,"recruitFaction", gameObject.getKey(),null, Enum.winAction.close).setBgColor(Enum.color.blue));
                }
            }else{//死亡后 可以【鞭尸】 【入狱】 【掠夺】
                choice.add(Choice.createChoice("鞭尸", Enum.messageType.action,"swipe", gameObject.getKey(),null).setBgColor(Enum.color.yellow));
                choice.add(Choice.createChoice("入狱", Enum.messageType.action,"imprison", gameObject.getKey(),null).setBgColor(Enum.color.yellow));
                choice.add(Choice.createChoice("掠夺", Enum.messageType.action,"rob", gameObject.getKey(),null).setBgColor(Enum.color.yellow));
            }

            getWinMsg().setChoice(choice);
            getWinMsg().setDesc(gameObject.getName());
            getClient().sendMsg(msgBuild(Enum.messageType.action, doTalk,object2JsonStr(getWinMsg()),gameObject.getKey()).getBytes());
        }else{//怪物npc等等的默认操作
            getClient().getScriptExecutor().load(gameObject.getScript() )
                    .execute(脚本_对话,getClient(),getWinMsg(),getMsg(),gameObject);
        }
    }
}
