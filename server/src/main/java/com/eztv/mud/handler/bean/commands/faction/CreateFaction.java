package com.eztv.mud.handler.bean.commands.faction;

import com.eztv.mud.DataBase;
import com.eztv.mud.GameUtil;
import com.eztv.mud.PropertiesUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.bean.commands.BaseCommand;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BString;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.chat;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-22 11:14
 用处：创建门派
**/
public class CreateFaction extends BaseCommand {
    public CreateFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        if(getMsg().getRole()!=null&&getMsg().getMsg()!=null){//开始建立
            if(getMsg().getRole().equals("do")){
                String factionName = getMsg().getMsg();
                if(BString.isChinese(factionName)&&factionName.length()>1&&factionName.length()<7){//执行创建
                    int factionId = BDate.getNowMillsByTen();
                    Faction faction = new Faction();
                    faction.setId(factionId);
                    faction.setName(factionName);
                    faction.setLevel(1);
                    faction.setDesc("");
                    try{
                        //创建帮派
                        DataBase.getInstance().init().query(faction).insert();
                        //设置自身的帮派id为当前帮派id
                        getClient().getPlayer().setFaction(factionId);
                    }catch (Exception e){//帮派名称已存在

                    }
                    //保存玩家状态
                    getClient().getPlayer().getDataBaseHandler().savePlayer(getClient().getPlayer());
                }else{
                    winMsg.setDesc(colorString(String.format(PropertiesUtil.getInstance().getProp().get("faction_create_norm").toString())));
                    choice.add(Choice.createChoice("原来如此", Enum.messageType.action,"got", null,null,true));
                    winMsg.setChoice(choice);
                    GameUtil.sendToSelf(getClient(),msgBuild(Enum.messageType.pop, "",object2JsonStr(winMsg),getMsg().getMsg(),null));
                }
                return;
            }
        }
        winMsg.setDesc(colorString(String.format(PropertiesUtil.getInstance().getProp().get("faction_create_notice").toString())));
        choice.add(Choice.createChoice("建立", Enum.messageType.action,"createFaction", null,"do",true));
        winMsg.setChoice(choice);
        GameUtil.sendToSelf(getClient(),msgBuild(Enum.messageType.input, "createFaction",object2JsonStr(winMsg),getMsg().getMsg(),null));
    }
}
