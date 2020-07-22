package com.eztv.mud.handler.bean.commands.action;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.bean.commands.BaseCommand;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;

public class UsePanel extends BaseCommand {
    public UsePanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        int itemId =-1;
        try {
            itemId = Integer.parseInt( getMsg().getMsg());
        }catch (Exception e){return;}
        Item item = findItemById(itemId);
        if(item==null)return;
        WinMessage winMsg = new WinMessage();
        winMsg.setCol(3);
        List<Choice> choice = new ArrayList<>();//背包物品以选择显示
        choice.add(Choice.createChoice("使用", Enum.messageType.action,"item_use",itemId+"",null,true));
        choice.add(Choice.createChoice("查看", Enum.messageType.action,"item_look",itemId+"",null));
        choice.add(Choice.createChoice("丢弃", Enum.messageType.action,"item_drop",itemId+"",null,true));
        choice.add(Choice.createChoice("全部丢弃", Enum.messageType.action,"item_drop",itemId+"","all",true));
        winMsg.setChoice(choice);
        winMsg.setDesc(item.getName());//显示当前玩家的金钱。元宝等等 交易信息。
        getClient().sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());
    }
}
