package com.eztv.mud.command.commands.faction;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.manager.FactionManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.FactionMembersSql;
import static com.eztv.mud.GameUtil.*;

/**
 * 作者：hhx QQ1025334900
 * 日期: 2020-07-22 11:14
 * 用处：门派成员
 **/
public class MemberFaction extends BaseCommand {
    public MemberFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        //查询所有成员
        List<Player> players =  DataBase.getInstance().init().
                createSQL(FactionMembersSql,getPlayer().getFaction()).list(Player.class);
        BDebug.trace("测试"+players.size());
        for (Player player:players) {
            choice.add(Choice.createChoice(getPropByFile("faction","faction_member",
                    player.getName(),
                    player.getLevel(),
                    FactionManager.getPositionAlias(player.getFaction_position())
                    ), getMsg().getType(), "grantMemberFaction", player.getAccount()+"", null));
        }
        winMsg.setCol(1);
        winMsg.setDesc(getPropByFile("faction","faction_member_title"));
        winMsg.setChoice(choice);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(winMsg), getMsg().getMsg(), null));
    }
}
