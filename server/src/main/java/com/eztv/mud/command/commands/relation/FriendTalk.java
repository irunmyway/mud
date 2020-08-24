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
import static com.eztv.mud.constant.Enum.winAction.close;
import static com.eztv.mud.constant.Enum.winAction.open;

public class FriendTalk extends BaseCommand {
    public FriendTalk(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        Msg msg = new Msg();
        getWinMsg().setDesc("对好友进行操作");
        getWinMsg().setCol(2);
        List<Choice> choice = new ArrayList<>();
        choice.add( Choice.createChoice(
                "私聊",
                Enum.messageType.input,
                "私聊",  getMsg().getMsg(), null, open
        ).setBgColor(Enum.color.gray));
        choice.add( Choice.createChoice(
                "组队",
                Enum.messageType.action,
                "friendBreak", null, null, open
        ).setBgColor(Enum.color.blue));
        msg.setCmd("friendBreak");
        msg.setMsg(getMsg().getMsg());
        msg.setRole(getMsg().getRole());
        choice.add( Choice.createChoice(
                "删除",
                Enum.messageType.pop,"confirmPanel",
                object2JsonStr(msg), "确定删除好友吗?", close
        ).setBgColor(Enum.color.red));
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
