package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Client;
import com.eztv.mud.cache.manager.ClientManager;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.constant.Enum.messageType;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.constant.Cmd.getGG;
import static com.eztv.mud.handler.event.player.msg.DeadMsg.showPanel;

public class GameHandler {
    //获取公告
    public static void getGG(Client client) {
        if(ClientManager.isDead(client,client.getPlayer())>0) {//死亡等等
            showPanel(client.getPlayer());
        }
        Chat chat = new Chat(Enum.chat.系统,Word.getInstance().getGG());
        client.sendMsg(msgBuild(messageType.normal, getGG,object2JsonStr(chat),"").getBytes());
    }
}
