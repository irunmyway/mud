package com.eztv.mud.handler.bean.commands;

import com.eztv.mud.PropertiesUtil;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.doTalk;

public class MinePanel extends BaseCommand{
    public MinePanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        String str =colorString(String.format(PropertiesUtil.getInstance().getProp().get("my_state").toString(),
                getClient().getPlayer().getName(),
                getClient().getPlayer().getAttribute().getAtk(),
                getClient().getPlayer().getAttribute().getDef(),
                getClient().getPlayer().getAttribute().getAcc(),
                getClient().getPlayer().getAttribute().getEva()
        ));
        winMsg.setDesc(str);//显示当前玩家状态
        choice.add(Choice.createChoice("我的装备", Enum.messageType.pop,"my_equip", null,null));
        choice.add(Choice.createChoice("门派", Enum.messageType.pop,"factionPanel", null,null));
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.action, doTalk,object2JsonStr(winMsg),null).getBytes());
    }
}
