package com.eztv.mud.command.commands.relation;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.cache.manager.FactionManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.constant.Cmd.doTalk;
import static com.eztv.mud.constant.Enum.winAction.*;

public class EnemyTalk extends BaseCommand {
    public EnemyTalk(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        Msg msg = new Msg();
        getWinMsg().setDesc("对仇人进行操作");
        getWinMsg().setCol(2);
        List<Choice> choice = new ArrayList<>();
        choice.add( Choice.createChoice(
                "私聊",
                Enum.messageType.input,
                "私聊",  getMsg().getMsg(), null, open
        ).setBgColor(Enum.color.gray));
        msg.setCmd("enemyBreak");
        msg.setMsg(getMsg().getMsg());
        msg.setRole(getMsg().getRole());
        choice.add( Choice.createChoice(
                "删除",
                Enum.messageType.pop,"confirmPanel",
                object2JsonStr(msg), "确定删除吗?", close
        ).setBgColor(Enum.color.red));
        choice.add( Choice.createChoice(
                "踪迹",
                Enum.messageType.action,
                "enemyTrace",  getMsg().getMsg(), null, closeAll
        ).setBgColor(Enum.color.blue));
        //帮派招募
        Faction faction = FactionManager.getFaction(getClient());
        if(faction!=null&&getPlayer().getFaction_position()>0){//管理层可以招募
            choice.add(Choice.createChoice("招募", Enum.messageType.action,"recruitFaction", getMsg().getMsg(),null, Enum.winAction.close).setBgColor(Enum.color.yellow));
        }
        getWinMsg().setChoice(choice);
        getClient().sendMsg(msgBuild(
                Enum.messageType.pop,
                doTalk,
                object2JsonStr(getWinMsg()),
                "unHand").getBytes());
    }
}
