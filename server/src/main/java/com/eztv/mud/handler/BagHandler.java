package com.eztv.mud.handler;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.WinMessage;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.bean.Cmd.doTAlk;

public class BagHandler {

    public static void getBag(Client client, Msg msg) {//获取背包
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();//背包物品以选择显示

        String desc = "";
        Bag bagDesc = client.getPlayer().getPlayerData().getBag();
        desc+=bagDesc.getMoney()+"&emsp; 金币:";
        desc+=bagDesc.getJbMoney()+"&emsp; 元宝:";
        desc+=bagDesc.getYbMoney();
        winMsg.setCol(3);
        winMsg.setChoice(choice);
        winMsg.setDesc("背包系统</p><br>&emsp; 铜币:"+desc);//显示当前玩家的金钱。元宝等等 交易信息。
        client.sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());

    }
}
