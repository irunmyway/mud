package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.net.Player;

import java.util.HashMap;

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
}
