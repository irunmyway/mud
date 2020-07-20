package com.eztv.mud.handler;

import com.eztv.mud.GameUtil;
import com.eztv.mud.PropertiesUtil;
import com.eztv.mud.Word;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.eztv.mud.Constant.Item_PATH;
import static com.eztv.mud.GameUtil.*;

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
            choice.add(Choice.createChoice(item.getName()+(item.getNum()<2?"":" *"+item.getNum()), Enum.messageType.action,"useClick",item.getId()+"",null,false));
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
        choice.add(Choice.createChoice("使用", Enum.messageType.action,"item_use",itemId+"",null,true));
        choice.add(Choice.createChoice("查看", Enum.messageType.action,"item_look",itemId+"",null));
        choice.add(Choice.createChoice("丢弃", Enum.messageType.action,"item_drop",itemId+"",null,true));
        choice.add(Choice.createChoice("全部丢弃", Enum.messageType.action,"item_drop",itemId+"","all",true));
        winMsg.setChoice(choice);
        winMsg.setDesc(item.getName());//显示当前玩家的金钱。元宝等等 交易信息。
        client.sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());

    }
    public static void item_use(Client client, Msg msg) {//使用
        WinMessage winMsg = new WinMessage();
        //执行物品脚本
        Item item = getItemById(msg.getMsg());
        if(item==null)return;
        LuaValue clientLua  = CoerceJavaToLua.coerce(client);
        LuaValue itemLua  = CoerceJavaToLua.coerce(item);
        LuaValue winLua  = CoerceJavaToLua.coerce(winMsg);
        LuaValue msgLua  = CoerceJavaToLua.coerce(msg);
        LuaValue[] objs = { clientLua, itemLua,winLua,msgLua};
        client.getScriptExecutor().loadfile(item.getScript() + ".lua").call();
        client.getScriptExecutor().get(LuaValue.valueOf("item_use")).invoke(objs);
    }
    public static void item_look(Client client, Msg msg){//查看物品
        WinMessage winMsg = new WinMessage();
        //执行物品脚本
        Item item = getItemById(msg.getMsg());
        if(item==null)return;
        LuaValue clientLua  = CoerceJavaToLua.coerce(client);
        LuaValue itemLua  = CoerceJavaToLua.coerce(item);
        LuaValue winLua  = CoerceJavaToLua.coerce(winMsg);
        LuaValue msgLua  = CoerceJavaToLua.coerce(msg);
        LuaValue[] objs = { clientLua, itemLua,winLua,msgLua};
        client.getScriptExecutor().loadfile(item.getScript() + ".lua").call();
        client.getScriptExecutor().get(LuaValue.valueOf("item_look")).invoke(objs);
    }
    public static void my_equip(Client client, Msg msg) {//查看装备
        WinMessage winMsg = new WinMessage();
        Equip equip = client.getPlayer().getPlayerData().getEquip();
        winMsg.setCol(2);
        winMsg.setDesc("我的装备");//显示当前玩家的金钱。元宝等等 交易信息。
        List<Choice> choice = new ArrayList<>();//装备集合
        choice.add(Choice.createChoice((equip.getWeapon().getName()==null?"武器:未装备":"武器:"+equip.getWeapon().getName()), Enum.messageType.action,"item_look",equip.getWeapon().getId()+"","weapon",true));
        choice.add(Choice.createChoice((equip.getHead().getName()==null?"头部:未装备":"头部:"+equip.getHead().getName()), Enum.messageType.action,"item_look",equip.getHead().getId()+"","head",true));
        choice.add(Choice.createChoice((equip.getCloth().getName()==null?"衣服:未装备":"衣服:"+equip.getCloth().getName()), Enum.messageType.action,"item_look",equip.getCloth().getKey(),"cloth",true));
        choice.add(Choice.createChoice((equip.getPants().getName()==null?"裤子:未装备":"裤子:"+equip.getPants().getName()), Enum.messageType.action,"item_look",equip.getPants().getKey(),"pants",true));
        choice.add(Choice.createChoice((equip.getShoes().getName()==null?"靴子:未装备":"靴子:"+equip.getShoes().getName()), Enum.messageType.action,"item_look",equip.getShoes().getKey(),"shoes",true));
        winMsg.setChoice(choice);
        client.sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());
    }
    public static void item_unload(Client client, Msg msg) {//卸载装备
        Equip equip = client.getPlayer().getPlayerData().getEquip();
        List<Item> list = new ArrayList<>();
        list.add(equip.getCloth());
        list.add(equip.getHead());
        list.add(equip.getWeapon());
        list.add(equip.getPants());
        list.add(equip.getShoes());
        int id = 0;
        Enum.equipType type = Enum.equipType.weapon;
        Properties Config = PropertiesUtil.getInstance().getProp();
        String str =colorString(String.format(Config.get("equip_drop").toString()));
        for(Item item:list){
            if((item.getId()+"").equals(msg.getMsg())){
                id = item.getId();
                type = item.getEquipType();
            }
        }
        if(id>0)
        switch (type){
            case shoes:
                str+= equip.getShoes().getName();
                if(id>0)equip.setShoes(new Item());
                break;
            case cloth:
                str+= equip.getCloth().getName();
                if(id>0)equip.setCloth(new Item());
                break;
            case head:
                str+= equip.getHead().getName();
                if(id>0)equip.setHead(new Item());
                break;
            case pants:
                str+= equip.getPants().getName();
                if(id>0)equip.setPants(new Item());
                break;
            case weapon:
                str+= equip.getWeapon().getName();
                if(id>0)equip.setWeapon(new Item());
                break;
        }
        if(id>0){
            client.getPlayer().getPlayerData().getBag().addItem(id,1);
            Chat chat = new Chat();
            chat.setContent(str);
            chat.setMsgType(Enum.chat.系统);
            GameUtil.sendToSelf(client,msgBuild(Enum.messageType.chat, "公聊",object2JsonStr(chat),""));
        }
    }
    public static void item_drop(Client client, Msg msg) {//卸载装备
        Chat chat = new Chat();
        chat.setMsgType(Enum.chat.系统);
        int id = -1;
        if(BString.isNumber(msg.getMsg()))
        id = Integer.parseInt(msg.getMsg()) ;
        if(id>-1){
            Item item = findItemById(id);
            if(item!=null){
                if(msg.getRole()!=null){//丢弃全部
                    int num = client.getPlayer().getPlayerData().getBag().delItemBundle(id);
                    chat.setContent(colorString(String.format(PropertiesUtil.getInstance().getProp().get("item_drop").toString(),item.getName(),num)));
                }else{
                    client.getPlayer().getPlayerData().getBag().delItem(id,1);
                    chat.setContent(colorString(String.format(PropertiesUtil.getInstance().getProp().get("item_drop").toString(),item.getName(),1)));
                }
                GameUtil.sendToSelf(client,msgBuild(Enum.messageType.chat, "公聊",object2JsonStr(chat),""));
            }
        }

    }


}
