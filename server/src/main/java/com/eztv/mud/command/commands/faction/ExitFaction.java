package com.eztv.mud.command.commands.faction;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Faction;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.cache.manager.FactionManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import static com.eztv.mud.GameUtil.*;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-05 10:21
 * 功能: 退出门派
 **/
public class ExitFaction extends BaseCommand {
    public ExitFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        Faction faction = FactionManager.getFaction(getClient());
        getPlayer().setFaction(null);
        getPlayer().setFaction_position(0);
        getPlayer().getDataBaseHandler().savePlayer(getClient().getPlayer());
        String sendStr = "你离开了门派";
        //发送帮派解散成功消息
        if (faction != null)
            sendStr = getPropByFile("faction", "faction_exit",
                    getPlayer().getName(), faction.getName());
        Chat chat = Chat.system(sendStr);
        GameUtil.sendToFaction(faction.getId(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
    }
}
