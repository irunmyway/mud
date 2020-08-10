package com.eztv.mud.command.commands.faction;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.cache.FactionCache;
import com.eztv.mud.cache.PlayerCache;
import com.eztv.mud.cache.manager.FactionManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-05 23:23
 * 功能: 帮派授职
 **/
public class GrantMemberFaction extends BaseCommand {
    public GrantMemberFaction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        String targetAcc = getMsg().getMsg();
        Player target = PlayerCache.getPlayer(targetAcc);
        int mPos = getClient().getPlayer().getFaction_position();//我的职位
        if(getMsg().getRole()!=null&&target!=null){//开始授权
            if(mPos>2) {//大于堂主才可以授权
                int pos = 0;
                try{
                    pos = Integer.parseInt(getMsg().getRole())+1;
                }catch(Exception e){e.printStackTrace();}
                //被授职人的职位必须比 授职的人低
                if(pos<mPos&&target.getFaction_position()<mPos){
                    FactionManager.setPosition(target,pos);
                    String sendStr = "提升了职位";
                    sendStr = getPropByFile("faction","faction_grant",
                            target.getName(),
                            getClient().getPlayer().getName(),
                            FactionCache.grantAlias.get(pos-1));
                    Chat chat = new Chat();
                    chat.setContent(sendStr);
                    chat.setMsgType(Enum.chat.系统);
                    GameUtil.sendToFaction(getClient().getPlayer().getFaction(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));

                }
            }

            return;
        }
        if (target != null&&(!targetAcc.equals(getClient().getPlayer().getAccount()))&&getMsg().getMsg()!=null) {
            WinMessage winMsg = new WinMessage();
            List<Choice> choice = new ArrayList<>();
            if(mPos>2) {//大于堂主才可以授权
                for(int i = mPos-2;i>=0 ;i--){
                    choice.add(Choice.createChoice(FactionCache.grantAlias.get(i), Enum.messageType.pop,getMsg().getCmd(), getMsg().getMsg(),i+""));
                }
                winMsg.setCol(1);
                winMsg.setDesc(getPropByFile("faction", "faction_grant_title"));
                winMsg.setChoice(choice);
                GameUtil.sendToSelf(getClient(), msgBuild(Enum.messageType.pop, "", object2JsonStr(winMsg), getMsg().getMsg(), null));
            }
        }
    }
}
