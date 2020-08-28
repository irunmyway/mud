package com.eztv.mud.command.commands.relation;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.PlayerCache;
import com.eztv.mud.cache.manager.RelationManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import static com.eztv.mud.GameUtil.*;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-24 17:53
 * 功能: 删除好友
 **/
public class EnemyBreak extends BaseCommand {
    public EnemyBreak(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        Player player = PlayerCache.getPlayer(getMsg().getRole());
        RelationManager.delRelation(getPlayer().getAccount(), getMsg().getRole(), Enum.relation.仇人);
        RelationManager.delRelation(getMsg().getRole(), getPlayer().getAccount(), Enum.relation.仇人);
        String sendStr = getPropByFile("relation", "enemy_break",
                player.getName());
        Chat chat = Chat.system(sendStr);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
    }
}
