package com.eztv.mud.command.commands.relation;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.Relation;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.PlayerCache;
import com.eztv.mud.cache.RelationCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.cache.manager.ClientManager.isOnline;
import static com.eztv.mud.constant.Cmd.doTalk;
import static com.eztv.mud.constant.Enum.winAction.open;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-24 17:53
 * 功能: 好友面板
 **/
public class EnemyPanel extends BaseCommand {
    public EnemyPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        getWinMsg().setDesc("仇人列表");
        getWinMsg().setCol(2);
        List<Choice> choice = new ArrayList<>();
        List<Relation> relations = RelationCache.relationMap.get(getPlayer().getAccount());
        if (relations.size() > 0) {
            for (Relation relation : relations) {
                if (relation.getType() == Enum.relation.仇人) {
                    try {
                        Player player = PlayerCache.getPlayer(relation.getToRole());
                        Player targetPlayer = isOnline(player);
                        Choice preAdd = Choice.createChoice(
                                targetPlayer!=null ? player.getName() : player.getName() + "(离)",
                                Enum.messageType.action,
                                "enemyTalk",targetPlayer==null?null:targetPlayer.getKey(), player.getAccount(), open
                        );
                        if (targetPlayer!=null) {
                            preAdd.setBgColor(Enum.color.blue);
                        }else{
                            preAdd.setBgColor(Enum.color.yellow);
                        }
                        choice.add(preAdd);
                    } catch (Exception e) {
                    }
                }
            }
        } else {
            getWinMsg().setDesc("暂无仇人");
        }

        getWinMsg().setChoice(choice);
        getClient().sendMsg(msgBuild(
                Enum.messageType.pop,
                doTalk,
                object2JsonStr(getWinMsg()),
                "unHand").getBytes());
    }
}
