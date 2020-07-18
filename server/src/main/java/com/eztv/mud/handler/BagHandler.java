package com.eztv.mud.handler;

import com.eztv.mud.Word;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.bean.net.WinMessage;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.List;

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
        choice.add(Choice.createChoice("全部丢弃", Enum.messageType.action,"item_drop_all",itemId+"",null,true));
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


}
