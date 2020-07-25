package com.eztv.mud.command.commands.action;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.command.commands.BaseCommand;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class ConfirmPanel extends BaseCommand {
    public ConfirmPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        choice.add(Choice.createChoice("我决定了", Enum.messageType.action,getMsg().getMsg(), null,null, Enum.winAction.closeAll));
        winMsg.setDesc(getMsg().getRole());
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());
    }
}
