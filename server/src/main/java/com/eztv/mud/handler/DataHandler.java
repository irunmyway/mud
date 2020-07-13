package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class DataHandler {

    //获取角色信息。绑定游戏数据专用函数
    public static Player getPlayer(Client client , Player player) {
        if (client != null) {
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            if (player.getAttribute().getHp() < 1) {//玩家身上没有信息
                Attribute attribute = new Attribute();
                attribute.setHp(base.getHp());
                attribute.setMp(base.getMp());
                attribute.setExp(base.getExp());
                attribute.setHp_max(base.getHp());
                attribute.setExp_max(base.getExp());
                attribute.setMp_max(base.getMp());
                attribute.setAck(base.getAck());
                player.getPlayerData().setAttribute(attribute);
            }
        }
        return player;
    }

    //获取角色信息。绑定游戏数据专用函数
    public static void sendReward(Client client , List<String> list) {
        WinMessage winMessage = new WinMessage();
        winMessage.setFloatMessage(list);
        client.sendMsg(msgBuild(Enum.messageType.action,"reward",object2JsonStr(winMessage),null));
    }



}
