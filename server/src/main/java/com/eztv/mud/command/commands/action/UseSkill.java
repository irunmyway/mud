package com.eztv.mud.command.commands.action;

import com.eztv.mud.bean.*;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.List;

import static com.eztv.mud.GameUtil.*;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-26 20:52
 * 功能: 设置当前使用的技能
 **/
public class UseSkill extends BaseCommand {
    public UseSkill(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        String skillId = getMsg().getMsg();
        List<Skill> skills = getClient().getPlayer().getPlayerData().getSkill().getSkills();
        Skill preSkill = null;
        for (Skill skill : skills) {
            if((skill.getId()+"").equals(skillId)){
                preSkill = skill;
                break;
            }
        }
        if(getClient().getPlayer().getPlayerData().getSkill().getCurSkill()==(preSkill))return;
        getClient().getPlayer().getPlayerData().getSkill().setCurSkill(preSkill);
        Chat chat = new Chat();
        chat.setMsgType(Enum.chat.系统);
        chat.setContent(getProp("skill_cur_change",
                preSkill==null?"普通攻击":preSkill.getName()));
        sendToSelf(getClient(),msgBuild(Enum.messageType.chat, "公聊",object2JsonStr(chat),""));
        getClient().getPlayer().onAttributeChange();
    }
}
