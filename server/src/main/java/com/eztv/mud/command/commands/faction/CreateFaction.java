package com.eztv.mud.command.commands.faction;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BString;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.Other_PATH;
import static com.eztv.mud.Constant.脚本_初始化;
import static com.eztv.mud.GameUtil.*;

/**
 * 作者：hhx QQ1025334900
 * 日期: 2020-07-22 11:14
 * 用处：创建门派
 **/
public class CreateFaction extends BaseCommand {
    public CreateFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        if (getMsg().getRole() != null && getMsg().getMsg() != null) {//开始建立
            if (getMsg().getMsg().equals("do")) {
                String factionName = getMsg().getRole();
                if (BString.isChinese(factionName) && factionName.length() > 1 && factionName.length() < 7) {//执行创建
                    Faction faction = new Faction();
                    faction.setName(factionName);
                    faction.setLevel(1);
                    faction.setDesc("");
                    try {
                        //创建帮派
                        faction.setLeader(getPlayer().getAccount());
                        faction.setAlias();
                        DataBase.getInstance().init().query(faction).insert();
                        //设置自身的帮派id为当前帮派id
                        getPlayer().setFaction(faction.getId());
                        getPlayer().setFaction_position(5);
                        FactionCache.factions.put(faction.getId(),faction);
                        getClient().getScriptExecutor().load(Other_PATH+"faction")
                                .execute(脚本_初始化,getClient(),new WinMessage());
                        //发送创建帮派成功消息
                        String sendStr = getPropByFile("faction","faction_create_success",
                                factionName,
                                getClient().getPlayer().getName());
                        Chat chat = new Chat();
                        chat.setContent(sendStr);
                        chat.setMsgType(Enum.chat.系统);
                        GameUtil.sendToAll(msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));

                    } catch (Exception e) {//帮派名称已存在
                        winMsg.setDesc(getPropByFile("faction","faction_exist", factionName));
                        choice.add(Choice.createChoice("原来如此", Enum.messageType.action, "got", null, null, Enum.winAction.close));
                        winMsg.setChoice(choice);
                        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(winMsg), getMsg().getMsg(), null));
                    }
                    //保存玩家状态
                    getClient().getPlayer().getDataBaseHandler().savePlayer(getClient().getPlayer());
                } else {//创建失败提示
                    winMsg.setDesc(getPropByFile("faction","faction_create_norm"));
                    choice.add(Choice.createChoice("原来如此", Enum.messageType.action, "got", null, null, Enum.winAction.close));
                    winMsg.setChoice(choice);
                    GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(winMsg), getMsg().getMsg(), null));
                }
                return;
            }
        }
        winMsg.setDesc(getPropByFile("faction","faction_create_notice"));
        choice.add(Choice.createChoice("建立", Enum.messageType.action, "createFaction", null, "do", Enum.winAction.closeAll));
        winMsg.setChoice(choice);
        GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.input, "createFaction", object2JsonStr(winMsg), getMsg().getMsg(), null));
    }
}
