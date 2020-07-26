package com.eztv.mud.command.commands.action;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.command.commands.BaseCommand;

import java.util.List;

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
        List<Item> skills = getClient().getPlayer().getPlayerData().getSkill().getSkills();
        for (Item skill : skills) {
            if((skill.getId()+"").equals(skillId)){
                getClient().getPlayer().getPlayerData().getSkill().setCurSkill(skill);
                break;
            }
        }
        getClient().getPlayer().onAttributeChange();
    }
}
