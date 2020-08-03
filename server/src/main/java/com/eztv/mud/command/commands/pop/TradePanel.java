package com.eztv.mud.command.commands.pop;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.constant.Cmd.doTalk;
import static com.eztv.mud.constant.Enum.winAction.open;

public class TradePanel extends BaseCommand {
    public TradePanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        winMsg.setDesc("欢迎来到商城。");
        choice.add(Choice.createChoice("金币商城",
                Enum.messageType.action,
                "jbStore", null,null,open
        ).setBgColor(Enum.color.yellow));
        choice.add(Choice.createChoice("元宝商城",
                Enum.messageType.action,
                "ybStore", null,null,open
        ).setBgColor(Enum.color.red));
        choice.add(Choice.createChoice("寄卖商城",
                Enum.messageType.action,
                "jmStore", null,null,open
        ).setBgColor(Enum.color.blue));
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(
                Enum.messageType.pop,
                doTalk,
                object2JsonStr(winMsg),
                "unHand").getBytes());
    }
}
