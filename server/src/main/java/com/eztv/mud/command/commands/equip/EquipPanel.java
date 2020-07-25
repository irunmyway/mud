package com.eztv.mud.command.commands.equip;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Equip;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.command.commands.BaseCommand;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class EquipPanel extends BaseCommand {
    public EquipPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        Equip equip = getClient().getPlayer().getPlayerData().getEquip();
        winMsg.setCol(2);
        winMsg.setDesc("我的装备");//显示当前玩家的金钱。元宝等等 交易信息。
        List<Choice> choice = new ArrayList<>();//装备集合
        choice.add(Choice.createChoice((equip.getWeapon().getName()==null?"武器:未装备":"武器:"+equip.getWeapon().getName()), Enum.messageType.action,"item_look",equip.getWeapon().getId()+"","weapon", Enum.winAction.close));
        choice.add(Choice.createChoice((equip.getHead().getName()==null?"头部:未装备":"头部:"+equip.getHead().getName()), Enum.messageType.action,"item_look",equip.getHead().getId()+"","head", Enum.winAction.close));
        choice.add(Choice.createChoice((equip.getCloth().getName()==null?"衣服:未装备":"衣服:"+equip.getCloth().getName()), Enum.messageType.action,"item_look",equip.getCloth().getKey(),"cloth", Enum.winAction.close));
        choice.add(Choice.createChoice((equip.getPants().getName()==null?"裤子:未装备":"裤子:"+equip.getPants().getName()), Enum.messageType.action,"item_look",equip.getPants().getKey(),"pants", Enum.winAction.close));
        choice.add(Choice.createChoice((equip.getShoes().getName()==null?"靴子:未装备":"靴子:"+equip.getShoes().getName()), Enum.messageType.action,"item_look",equip.getShoes().getKey(),"shoes", Enum.winAction.close));
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());
    }
}
