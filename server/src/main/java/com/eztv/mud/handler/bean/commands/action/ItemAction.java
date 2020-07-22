package com.eztv.mud.handler.bean.commands.action;

import com.eztv.mud.GameUtil;
import com.eztv.mud.PropertiesUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.bean.commands.BaseCommand;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.GameUtil.object2JsonStr;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-21 23:41
 用处：物品查看
**/
public class ItemAction extends BaseCommand {


    public ItemAction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        switch (getMsg().getCmd()){
            case "item_look"://查看物品
                item_look();
                break;
            case "item_use"://物品使用
                item_use();
                break;
            case "item_unload"://卸载装备
                item_unload();
                getClient().getPlayer().onAttributeChange();
                break;
            case "item_drop"://丢弃物品
                item_drop();
                break;
        }

    }

    private void item_look(){
        WinMessage winMsg = new WinMessage();
        //执行物品脚本
        Item item = getItemById(getMsg().getMsg());
        if(item==null)return;
        getClient().getScriptExecutor().loadFile(GameObject.class,item.getScript() + ".lua")
        .execute("item_look",getClient(),item,winMsg,getMsg());
    }
    private void item_use(){
        WinMessage winMsg = new WinMessage();
        //执行物品脚本
        Item item = getItemById(getMsg().getMsg());
        if(item==null)return;
        getClient().getScriptExecutor().loadFile(GameObject.class,item.getScript() + ".lua")
                .execute("item_use",getClient(),item,winMsg,getMsg());
    }
    private void item_unload(){
        Equip equip = getClient().getPlayer().getPlayerData().getEquip();
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
            if((item.getId()+"").equals(getMsg().getMsg())){
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
            getClient().getPlayer().getPlayerData().getBag().addItem(id,1);
            Chat chat = new Chat();
            chat.setContent(str);
            chat.setMsgType(Enum.chat.系统);
            GameUtil.sendToSelf(getClient(),msgBuild(Enum.messageType.chat, "公聊",object2JsonStr(chat),""));
        }
    }
    private void item_drop(){
        Chat chat = new Chat();
        chat.setMsgType(Enum.chat.系统);
        int id = -1;
        if(BString.isNumber(getMsg().getMsg()))
            id = Integer.parseInt(getMsg().getMsg()) ;
        if(id>-1){
            Item item = findItemById(id);
            if(item!=null){
                if(getMsg().getRole()!=null){//丢弃全部
                    int num = getClient().getPlayer().getPlayerData().getBag().delItemBundle(id);
                    chat.setContent(colorString(String.format(PropertiesUtil.getInstance().getProp().get("item_drop").toString(),item.getName(),num)));
                }else{
                    getClient().getPlayer().getPlayerData().getBag().delItem(id,1);
                    chat.setContent(colorString(String.format(PropertiesUtil.getInstance().getProp().get("item_drop").toString(),item.getName(),1)));
                }
                GameUtil.sendToSelf(getClient(),msgBuild(Enum.messageType.chat, "公聊",object2JsonStr(chat),""));
            }
        }
    }



}
