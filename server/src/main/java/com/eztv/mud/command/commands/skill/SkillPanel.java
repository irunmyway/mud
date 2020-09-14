package com.eztv.mud.command.commands.skill;

import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;

public class SkillPanel extends BaseCommand {
    public SkillPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        Equip equip = getClient().getPlayer().getPlayerData().getEquip();
        winMsg.setCol(2);
        winMsg.setDesc("我的技能");//显示当前玩家的金钱。元宝等等 交易信息。
        List<Choice> choice = new ArrayList<>();//装备集合
        List<Skill> skills =  getPlayer().getPlayerData().getSkill().getSkills();
        skills.forEach(skill -> {
            choice.add(Choice.createChoice((
                    skill.getName()),
                    Enum.messageType.action,
                    "skill_look",skill.getId()+"",
                    "skill", Enum.winAction.close));
        });
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(Enum.messageType.pop, null,object2JsonStr(winMsg),null).getBytes());
    }
}
