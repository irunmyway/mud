package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Skill;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.script.ScriptExecutor;
import com.eztv.mud.utils.BDebug;

import java.util.List;

import static com.eztv.mud.Constant.Skill_PATH;
import static com.eztv.mud.Constant.脚本_初始化;

public class SkillCache {
    private static List<Skill> skills;

    public static void initSkill(ScriptExecutor scriptExecutor) {//初始化游戏物品
        skills = DataBase.getInstance().init().createSQL("select * from t_skill").list(Skill.class);
        for (Skill item : skills) {
            try {
                if (item.getScript().length() > 0) {//模板
                    item.setScript(Skill_PATH + item.getScript());
                    scriptExecutor.load(item.getScript());
                    scriptExecutor.execute(脚本_初始化,item);
                }
                item.setType(Enum.itemType.skill);
            } catch (Exception e) {e.printStackTrace();}
        }
        BDebug.trace("游戏技能加载完成 数量 : Skills load item_num:【" + skills.size() + "】");
    }


    public static List<Skill> getSkills() {
        return skills;
    }
}
