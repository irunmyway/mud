package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.WinMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.eztv.mud.GameUtil.*;
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
        for(Item item:bagDesc.getItems()){
            choice.add(Choice.createChoice(item.getName()+(item.getNum()<2?"":" *"+item.getNum()), Enum.messageType.action,"useClick",item.getId()+"",null));
        }
        winMsg.setChoice(choice);
        winMsg.setDesc("背包系统</p><br>&emsp; 铜币:"+desc);//显示当前玩家的金钱。元宝等等 交易信息。
        client.sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());

    }

    public static void useClick(Client client, Msg msg) {//用户点击了物品
        int itemId =-1;
        try {
            itemId = Integer.parseInt( msg.getMsg());
        }catch (Exception e){return;}
        Item item = findItemById(itemId);
        if(item==null)return;
        WinMessage winMsg = new WinMessage();
        winMsg.setCol(3);
        List<Choice> choice = new ArrayList<>();//背包物品以选择显示
        choice.add(Choice.createChoice("使用", Enum.messageType.action,"Item_use",itemId+"",null));
        choice.add(Choice.createChoice("查看", Enum.messageType.action,"Item_look",itemId+"",null));
        choice.add(Choice.createChoice("丢弃", Enum.messageType.action,"Item_drop",itemId+"",null));
        choice.add(Choice.createChoice("全部丢弃", Enum.messageType.action,"Item_drop_all",itemId+"",null));
        winMsg.setChoice(choice);
        winMsg.setDesc(item.getName());//显示当前玩家的金钱。元宝等等 交易信息。
        client.sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());

    }
}
