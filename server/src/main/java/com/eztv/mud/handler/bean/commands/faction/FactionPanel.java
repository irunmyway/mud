package com.eztv.mud.handler.bean.commands.faction;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.bean.commands.BaseCommand;
import com.eztv.mud.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.constant.Cmd.doTalk;

public class FactionPanel extends BaseCommand {
    public FactionPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
//        DataBase.getInstance().init().query()
        int faction = getClient().getPlayer().getFaction();
        winMsg.setDesc("门派");
        if(faction<1){//暂无门派
            choice.add(Choice.createChoice("创建门派", Enum.messageType.action,"createFaction", null,null,false));
            choice.add(Choice.createChoice("加入门派", Enum.messageType.action,"joinFaction", null,null));
        }else{//有门派了 查看门派信息

        }
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.action, doTalk,object2JsonStr(winMsg),null).getBytes());
    }
}
