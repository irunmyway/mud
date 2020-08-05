package com.eztv.mud.command.commands.faction;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.FactionListSql;
import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-22 11:14
 用处：加入帮派
**/
public class JoinFaction extends BaseCommand {
    public JoinFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        if (getMsg().getRole() != null && getMsg().getMsg() != null) {//发送加入门派申请
            if (getMsg().getRole().equals("do")) {

            }
        }
        WinMessage winMsg = new WinMessage();
        winMsg.setDesc("门派消息");
        winMsg.setCol(2);
        List<Choice> choice = new ArrayList<>();
        //查询所有门派
        List<Faction> factions =  DataBase.getInstance().init().createSQL(FactionListSql).list(Faction.class);
        for (Faction faction:factions) {
            choice.add(Choice.createChoice(faction.getName()+" /"+faction.getLevel(), getMsg().getType(), "", faction.getId()+"", "do", Enum.winAction.closeAll));
        }
        winMsg.setChoice(choice);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "createFaction", object2JsonStr(winMsg), getMsg().getMsg(), null));

    }
}
