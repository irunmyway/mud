package com.eztv.mud.handler.event.player.msg;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.manager.ClientManager;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class DeadMsg {
    public static void showPanel(GameObject diedObj){
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        Client dieClient = ClientManager.getClientByAccount(((Player) diedObj).getAccount());
        if (dieClient != null) {
            choice.add(Choice.createChoice("复活", Enum.messageType.action, "relive", null, null, Enum.winAction.open));
            winMsg.setChoice(choice);
            winMsg.setDesc("您已经死亡</p><br>&emsp;" + "请选择如何转生。");
            dieClient.sendMsg(msgBuild(Enum.messageType.unHandPop, "relive", object2JsonStr(winMsg), null).getBytes());
        }
    }
}
