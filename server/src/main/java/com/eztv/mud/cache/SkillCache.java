package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Item;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.Skill_PATH;

public class SkillCache {
    private static HashMap<String, LuaValue> skillScript = new HashMap<String, LuaValue>();
    private static List<Item> skills;

    public static void initSkill(Globals globals) {//初始化游戏物品
        skillScript.clear();
        skills = DataBase.getInstance().init().createSQL("select * from t_skill").list(Item.class);
        for (Item item : skills) {
            try {
                if (item.getScript().length() > 0) {//模板
                    item.setScript(Skill_PATH + item.getScript());
                    skillScript.put(item.getId() + "", globals.loadfile(item.getScript() + ".lua"));
                }
                item.setType(Enum.itemType.skill);
            } catch (Exception e) {e.printStackTrace();}
        }
        BDebug.trace("游戏技能加载完成 数量 : Skills load item_num:【" + skills.size() + "】");
    }














    public static List<Item> getSkills() {
        return skills;
    }
}
