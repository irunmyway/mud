package com.eztv.mud.command.commands.bag;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.command.commands.BaseCommand;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;

public class BagPanel  extends BaseCommand {
    public BagPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        String desc = "";
        Bag bagDesc = getClient().getPlayer().getPlayerData().getBag();
        desc = getPropByFile("bag","bag_panel_title",
                bagDesc.getMoney(),
                bagDesc.getJbMoney(),
                bagDesc.getYbMoney()
                );
        winMsg.setCol(2);
        for(Item item:bagDesc.getItems()){
            choice.add(Choice.createChoice(item.getName(),
                    Enum.messageType.pop,"useClick",
                    item.getId()+"",item.getType().toString()));
        }
        winMsg.setChoice(choice);
        winMsg.setDesc(desc);//显示当前玩家的金钱。元宝等等 交易信息。
        getClient().sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());
    }
}
