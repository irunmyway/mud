package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.PlayerData;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDebug;
import com.google.gson.Gson;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class DataHandler {
    static Gson gson = new Gson();

    //获取角色信息。绑定游戏数据专用函数
    public synchronized static void getPlayer(Client client, Player player) {
        if (client != null) {
            try {
                String data = new String(Base64.getDecoder().decode(player.getData()));
                PlayerData pd = gson.fromJson(data, PlayerData.class);
                player.setPlayerData(pd);
            } catch (Exception e) {
                e.printStackTrace();
                BDebug.trace("玩家没有数据:" + e.toString());
            }
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            if (player.getAttribute().getHp() < 1) {//玩家身上没有信息
                base.setHp(base.getHp_max());
                base.setMp(base.getMp_max());
                if (player.getLevel() > 1)
                    base.setExp(0);
                player.getPlayerData().setAttribute(base);
            }
        }
    }

    //获取角色信息。绑定游戏数据专用函数
    public static Player getPlayerByUpLevel(Object client, Player player) {
        if (client != null) {
            //获取玩家基础属性
            HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
            Attribute base = attributes.get(player.getLevel() + "");
            base.setHp(base.getHp_max());
            base.setMp(base.getMp_max());
            if (player.getLevel() < 2)
                base.setExp(0);
            player.getPlayerData().setAttribute(base);
        }
        return player;
    }


    //获取角色信息。绑定游戏数据专用函数
    public static void sendReward(Client client, List<String> list) {
        WinMessage winMessage = new WinMessage();
        winMessage.setFloatMessage(list);
        client.sendMsg(msgBuild(Enum.messageType.action, "reward", object2JsonStr(winMessage), null));
    }

    //获取基础属性//不包括当前血量等等
    public static Attribute getBaseAttribute(int level) {
        HashMap<String, Attribute> attributes = Word.getInstance().getBaseAttributes();
        Attribute base = attributes.get(level + "");
        Attribute attribute = new Attribute();
        return attribute.add(base);
    }

}
