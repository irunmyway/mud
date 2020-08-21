package com.eztv.mud.command.commands.faction;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.manager.ClientManager;
import com.eztv.mud.cache.manager.FactionManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.cache.manager.FactionManager.joinFaction;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-22 11:14
 用处：招募帮众
**/
public class RecruitFaction extends BaseCommand {
    public RecruitFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        if (getMsg().getRole() != null && getMsg().getMsg() != null) {//确定加入
            if (getMsg().getRole().equals("do")) {
                Faction faction = joinFaction(getClient(),getMsg().getMsg());
                if(faction!=null){
                    //发送创建帮派成功消息
                    String sendStr = getPropByFile("faction","faction_join",
                            getClient().getPlayer().getName(),faction.getName());
                    Chat chat = Chat.system(sendStr);
                    GameUtil.sendToFaction(getClient().getPlayer().getFaction(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
                }
            }
            return;
        }
        Client client =  ClientManager.getClient(getMsg().getMsg());
        if(client!=null){
           if( client.getPlayer().getFaction()>0){//已经加入帮派了
               //发送创建帮派成功消息
               String sendStr = getPropByFile("faction","faction_already_join",
                       client.getPlayer().getName());
               Chat chat = Chat.system(sendStr);
               GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
               return;
           }
            WinMessage winMsg = new WinMessage();
            List<Choice> choice = new ArrayList<>();
            Faction faction  = FactionManager.getFaction(getClient());
            int position = getPlayer().getFaction_position();
            if(position>0){//发送招募
                faction.getAllowJoin().put(getMsg().getMsg(),"1");
                winMsg.setDesc(getPropByFile("faction","faction_invite",getPlayer().getName()));
                choice.add(Choice.createChoice("同意加入", Enum.messageType.action, getMsg().getCmd(), faction.getId()+"", "do", Enum.winAction.close).setBgColor(Enum.color.blue));
                winMsg.setCol(2);
                winMsg.setChoice(choice);
            }
            GameUtil.sendToKey(getMsg().getMsg(), msgBuild(Enum.messageType.pop, "joinFaction", object2JsonStr(winMsg), getMsg().getMsg(), null));
        }
    }
}
