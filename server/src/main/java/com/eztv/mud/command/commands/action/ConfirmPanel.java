package com.eztv.mud.command.commands.action;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;

public class ConfirmPanel extends BaseCommand {
    public ConfirmPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        Msg msg = null;
        try {
            msg = JSONObject.toJavaObject(jsonStr2Json(getMsg().getMsg()), Msg.class);
        } catch (Exception e) {
        }
        if (msg != null) {
            List<Choice> choice = new ArrayList<>();
            choice.add(Choice.createChoice("我决定了", Enum.messageType.action,msg.getCmd() , msg.getMsg(), msg.getRole(), Enum.winAction.closeAll));
            getWinMsg().setDesc(getMsg().getRole());
            getWinMsg().setChoice(choice);
            getClient().sendMsg(msgBuild(Enum.messageType.pop, null, object2JsonStr(getWinMsg()), null).getBytes());

        }
    }
}
