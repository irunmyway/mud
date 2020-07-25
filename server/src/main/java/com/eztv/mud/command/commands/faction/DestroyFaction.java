package com.eztv.mud.command.commands.faction;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BProp;

import static com.eztv.mud.GameUtil.*;

public class DestroyFaction extends BaseCommand {
    public DestroyFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        int flag = DataBase.getInstance().init().createSQL("delete from t_faction where id = '"+getClient().getPlayer().getFaction()+"'")
                .update();
        if(flag>0){
            FactionCache.remove(getClient().getPlayer().getFaction());//从缓存中移除
            getClient().getPlayer().setFaction(0);
            getClient().getPlayer().getDataBaseHandler().savePlayer(getClient().getPlayer());
            //发送帮派解散成功消息
            String sendStr = colorString(String.format(BProp.getInstance().getProp().get("faction_destroy_success").toString(),  getClient().getPlayer().getName(),""));
            Chat chat = new Chat();
            chat.setContent(sendStr);
            chat.setMsgType(Enum.chat.系统);
            GameUtil.sendToAll(getClient(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
        }
    }
}
