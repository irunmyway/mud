package com.eztv.mud.command.commands;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.doTalk;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-23 14:53
 * 功能: 我的面板
 **/
public class MinePanel extends BaseCommand{
    public MinePanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        String str = getProp("my_state",
                getClient().getPlayer().getName(),
                getClient().getPlayer().getAttribute().getAtk(),
                getClient().getPlayer().getAttribute().getDef(),
                getClient().getPlayer().getAttribute().getAcc(),
                getClient().getPlayer().getAttribute().getEva());
        winMsg.setDesc(str);//显示当前玩家状态
        choice.add(Choice.createChoice("我的装备", Enum.messageType.pop,"my_equip", null,null, Enum.winAction.open).setBgColor(Enum.color.red));
        choice.add(Choice.createChoice("门派", Enum.messageType.pop,"factionPanel", null,null, Enum.winAction.open).setBgColor(Enum.color.blue));
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.action, doTalk,object2JsonStr(winMsg),null).getBytes());
    }
}
