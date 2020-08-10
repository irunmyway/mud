package com.eztv.mud.cache.manager;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Item;
import com.eztv.mud.cache.SkillCache;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-08 19:30
 * 功能: 游戏物品处理
 **/
public class ItemManager {
    //通过静态id查找游戏物品
    public static Item getItemById(String id) {
        Item item = null;
        for(int i = 0; i< Word.getInstance().getItems().size(); i++){
            if((Word.getInstance().getItems().get(i).getId()+"").equals(id)){
                item = Word.getInstance().getItems().get(i);
            }
        }
        return item;
    }
    //通过静态id查找游戏技能
    public static Item getSkillById(String id) {
        Item item = null;
        for(int i = 0; i< SkillCache.getSkills().size(); i++){
            if((SkillCache.getSkills().get(i).getId()+"").equals(id)){
                item = SkillCache.getSkills().get(i);
            }
        }
        return item;
    }
}
