package com.eztv.mud.command.commands.pop;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.constant.Cmd.doTalk;
import static com.eztv.mud.constant.Enum.winAction.*;

public class SkillAttackPanel extends BaseCommand {
    public SkillAttackPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        winMsg.setDesc("使用技能");
        List<Item> skills = getClient().getPlayer().getPlayerData().getSkill().getSkills();
        choice.add(Choice.createChoice(
               "普通攻击",
                Enum.messageType.action,
                "useSkill", null,null,open));
        for (Item skill :skills) {
            choice.add(Choice.createChoice(
                    skill.getName(),
                    Enum.messageType.action,
                    "useSkill", skill.getId()+"",null,open));
        }
        choice.add(Choice.createChoice(
                "『关闭』",
                Enum.messageType.action,
                "", null,null,closeAll).setBgColor(Enum.color.red));
        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(
                Enum.messageType.mapPop,
                doTalk,
                object2JsonStr(winMsg),
                "unHand").getBytes());
    }
}
