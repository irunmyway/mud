package com.eztv.mud.command.commands.relation;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.constant.Cmd.doTalk;
import static com.eztv.mud.constant.Enum.winAction.open;

public class RelationPanel extends BaseCommand {
    public RelationPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        getWinMsg().setDesc("我的关系");
        getWinMsg().setCol(3);
        List<Choice> choice = new ArrayList<>();
        choice.add(Choice.createChoice("好友",
                Enum.messageType.pop,
                "好友列表", null,null,open
        ).setBgColor(Enum.color.yellow));
        choice.add(Choice.createChoice("徒弟",
                Enum.messageType.action,
                "徒弟列表", null,null,open
        ).setBgColor(Enum.color.blue));
        choice.add(Choice.createChoice("仇人",
                Enum.messageType.pop,
                "仇人列表", null,null,open
        ).setBgColor(Enum.color.red));
        getWinMsg().setChoice(choice);
        getClient().sendMsg(msgBuild(
                Enum.messageType.pop,
                doTalk,
                object2JsonStr(getWinMsg()),
                "unHand").getBytes());
    }
}
