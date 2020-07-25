package com.eztv.mud.cache;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.DataBase;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.onObjectInRoom;

public class MonsterCache {
    private static List<Monster> monsters;
    private static HashMap<String, LuaValue> monsterScript = new HashMap<String, LuaValue>();

    public static void initMonster(Globals globals, HashMap<String, Room> Rooms) {//加载所有怪物 和他的脚本
        monsterScript.clear();
        monsters = DataBase.getInstance().init().createSQL("select * from t_monster").list(Monster.class);
        for (Monster monster : monsters) {
            try {
                Attribute attribute = new Attribute();
                //List<Choice> choice = new ArrayList<>();
                if (monster.getScript().length() > 0) {//模板
                    monster.setScript(Monster_PATH + monster.getScript());
                    monsterScript.put(monster.getId() + "", globals.loadfile(monster.getScript() + ".lua"));
                    globals.load(monsterScript.get(monster.getId() + ""));
                    attribute = JSONObject.toJavaObject(jsonStr2Json(globals.get(LuaValue.valueOf(LUA_初始化)).invoke().toString()), Attribute.class);
                    monster.setAttribute(attribute);
                }
                final Attribute baseAttribute = attribute;
                for (int i = 0; i < monster.getNum(); i++) {//实例化怪物
                    Monster m = (Monster) monster.clone();
                    m.setAttribute((Attribute) baseAttribute.clone());
                    m.setKey(BDate.getNowMills() + i + "m");
                    m.setScript(monster.getScript());
                    if (m.getRefreshment() > 0) {
                        m.setiGameObject((client) -> new Thread(new Runnable() {//怪物复活回调
                            @Override
                            public void run() {
                                GameObject addM;
                                try {
                                    Thread.sleep(m.getRefreshment());
                                    addM = (Monster) m.clone();
                                    addM.setAttribute((Attribute) baseAttribute.clone());
                                    addM.setScript(monster.getScript());
                                    addM.setKey(BDate.getNowMills() + "m");
                                    Rooms.get(m.getMap() + "").add(addM);
                                    for (Client item : clients) {//发送房间东西新增
                                        try {
                                            if (item.getPlayer().getPlayerData().getRoom().equals(m.getMap() + "")) {
                                                item.sendMsg(msgBuild(Enum.messageType.normal, onObjectInRoom, object2JsonStr(addM.toSendGameObject()), null));
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start());
                    }
                    Rooms.get(m.getMap() + "").add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BDebug.trace("怪物加载完成 数量 : Monster load monster_num:【" + monsters.size() + "】");
    }

    public static List<Monster> getMonsters() {
        return monsters;
    }
}
