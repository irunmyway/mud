package com.eztv.mud.command.commands.action;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BString;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.cache.manager.ItemManager.getItemById;
import static com.eztv.mud.cache.manager.ItemManager.getSkillById;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-21 23:41
 用处：物品查看
**/
public class SkillAction extends BaseCommand {


    public SkillAction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        switch (getMsg().getCmd()){
            case "skill_look"://查看物品
                skill_look();
                break;
        }

    }

    private void skill_look(){
        WinMessage winMsg = new WinMessage();
        //执行物品脚本
        Item item=getSkillById(getMsg().getMsg());
        Skill skill=item instanceof Skill?(Skill)item:null;
        if(item==null)return;
        getClient().getScriptExecutor().load(Init_PATH+"/panel/skill")
        .execute(脚本_物品查看,getClient(),item,winMsg,getMsg());
    }
    private void item_use(){
        WinMessage winMsg = new WinMessage();
        //执行物品脚本
        Item item=null;
        if (getMsg().getRole().equals("skill")){
            item = getSkillById(getMsg().getMsg());
        }else{
            item = getItemById(getMsg().getMsg());
        }
        if(item==null)return;
        getClient().getScriptExecutor().load(item.getScript())
                .execute(脚本_物品使用,getClient(),item,winMsg,getMsg());
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
        String str =getProp("equip_drop");
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
            getClient().getPlayer().getPlayerData().getBag().giveItem(id,1);
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
                    chat.setContent(getProp("item_drop",item.getName(),
                            num));
                }else{
                    getClient().getPlayer().getPlayerData().getBag().delItem(id,1);
                    chat.setContent(getProp("item_drop",item.getName(),1));
                }
                GameUtil.sendToSelf(getClient(),msgBuild(Enum.messageType.chat, "公聊",object2JsonStr(chat),""));
            }
        }
    }



}
