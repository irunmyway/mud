package com.eztv.mud.command.commands.faction;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import static com.eztv.mud.GameUtil.*;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-05 10:21
 * 功能: 解散门派
 **/
public class DestroyFaction extends BaseCommand {
    public DestroyFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        if(getPlayer().getFaction_position()!=5){//不是帮主
            //发送帮派解散成功消息
            String sendStr = getPropByFile("faction","faction_destroy_fail_grant",
                    getClient().getPlayer().getName(),"");
            Chat chat = new Chat();
            chat.setContent(sendStr);
            chat.setMsgType(Enum.chat.系统);
            GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
            return;
        }
        int flag = DataBase.getInstance().init().createSQL("delete from t_faction where id = '"+getClient().getPlayer().getFaction()+"'")
                .update();
        if(flag>0){
            FactionCache.remove(getClient().getPlayer().getFaction());//从缓存中移除
            getClient().getPlayer().setFaction(-1);
            getClient().getPlayer().setFaction_position(0);
            getClient().getPlayer().getDataBaseHandler().savePlayer(getClient().getPlayer());
            //发送帮派解散成功消息
            String sendStr = getPropByFile("faction","faction_destroy_success",
            getClient().getPlayer().getName(),"");
            Chat chat = new Chat();
            chat.setContent(sendStr);
            chat.setMsgType(Enum.chat.系统);
            GameUtil.sendToAll(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
        }
    }
}
