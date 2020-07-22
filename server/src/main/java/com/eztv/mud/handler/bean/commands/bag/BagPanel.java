package com.eztv.mud.handler.bean.commands.bag;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.bean.commands.BaseCommand;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

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
        desc+=bagDesc.getMoney()+"&emsp; 金币:";
        desc+=bagDesc.getJbMoney()+"&emsp; 元宝:";
        desc+=bagDesc.getYbMoney();
        winMsg.setCol(3);
        for(Item item:bagDesc.getItems()){
            choice.add(Choice.createChoice(item.getName()+(item.getNum()<2?"":" *"+item.getNum()), Enum.messageType.pop,"useClick",item.getId()+"",null,false));
        }
        winMsg.setChoice(choice);
        winMsg.setDesc("背包</p><br>&emsp; 铜币:"+desc);//显示当前玩家的金钱。元宝等等 交易信息。
        getClient().sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());
    }
}
