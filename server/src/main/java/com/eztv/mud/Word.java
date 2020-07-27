package com.eztv.mud;

import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.Room;
import com.eztv.mud.cache.*;
import com.eztv.mud.command.CommandSetHandler;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BFile;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.cache.RoomCache.Rooms;

public class Word {
    private HashMap<String, Attribute> baseAttributes = new HashMap<String, Attribute>();
    private Globals globals = JsePlatform.standardGlobals();
    private String GG = "";
    private static Word Instance;

    //获取单例
    public static Word getInstance() {
        if (Instance == null) Instance = new Word();
        return Instance;
    }
    public void init() {
        initGG();//加载公告
        RoomCache.initRooms();//加载房间
        NpcCache.initNPC(globals,Rooms);//加载NPC
        MonsterCache.initMonster(globals,Rooms);//加载怪物
        FactionCache.initFactionCache();//加载行会
        ItemCache.initItem(globals);//加载物品
        SkillCache.initSkill(globals);//加载技能
        initBaseAttribute();//加载基础属性
        initHandler();//装载指令
    }


    public void initGG() {//加载公告
        String src = System.getProperty("user.dir")+"/gg";
        GG = BFile.readFromFile(src);
        BDebug.trace("公告加载完成");
    }

    private void initBaseAttribute() {
        //加载属性上线 基础公式表
        baseAttributes.clear();
        List<Attribute> attributeList = DataBase.getInstance().init().createSQL("select * from t_attribute").list(Attribute.class);
        for (Attribute attribute : attributeList) {
            baseAttributes.put(attribute.getLevel() + "", attribute);
        }
        BDebug.trace("基础属性加载完成 最高等级 : Attribute load max_level:【" + baseAttributes.size() + "】");
    }

    //装载指令
    private void initHandler() {
        CommandSetHandler.initActionCommandSet();
        CommandSetHandler.initPanelCommandSet();
        BDebug.trace("装载游戏指令完成");
    }


    public HashMap<String, Room> getRooms() {
        return Rooms;
    }

    public HashMap<String, Attribute> getBaseAttributes() {
        return baseAttributes;
    }

    public List<Item> getItems() {
        return ItemCache.getItems();
    }

    public String getGG() {
        return GG;
    }
}
