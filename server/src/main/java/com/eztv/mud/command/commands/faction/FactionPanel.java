package com.eztv.mud.command.commands.faction;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
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
            choice.add(Choice.createChoice("创建门派", Enum.messageType.action,"createFaction", null,null).setBgColor(Enum.color.blue));
            choice.add(Choice.createChoice("加入门派", Enum.messageType.action,"joinFaction", null,null).setBgColor(Enum.color.yellow));
        }else{//有门派了 查看门派信息
            Faction mFaction = FactionCache.factions.get(faction+"");
            String str=getProp("faction_panel",
                        mFaction.getName(),
                        mFaction.getLevel(),
                        mFaction.getDesc()
                    );
            winMsg.setDesc(str);
            choice.add(Choice.createChoice("解散门派", Enum.messageType.pop,"confirmPanel", "destroyFaction","这将会解散你的帮派，你确定吗？")
            .setBgColor(Enum.color.red));
        }
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.action, doTalk,object2JsonStr(winMsg),null).getBytes());
    }
}
