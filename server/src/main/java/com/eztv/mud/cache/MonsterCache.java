package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.*;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.script.ScriptExecutor;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BDebug;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.msgBuild;
import static com.eztv.mud.GameUtil.object2JsonStr;
import static com.eztv.mud.constant.Cmd.onObjectInRoom;

public class MonsterCache {
    private static List<Monster> monsters;
    private static HashMap<String, String> monsterScript = new HashMap<String, String>();

    public static void initMonster(ScriptExecutor scriptExecutor, HashMap<String ,HashMap<String, Room>> Rooms) {//加载所有怪物 和他的脚本
        monsterScript.clear();
        monsters = DataBase.getInstance().init().createSQL("select * from t_monster").list(Monster.class);
        for (Monster monster : monsters) {
            try {
                Attribute attribute = new Attribute();
                if (monster.getScript().length() > 0) {//模板
                    monster.setScript(Monster_PATH + monster.getScript());
                    monsterScript.put(monster.getId() + "", monster.getScript());
                    scriptExecutor.load(monsterScript.get(monster.getId() + ""));
                    scriptExecutor.execute(脚本_初始化,attribute);
//                    attribute = JSONObject.toJavaObject(jsonStr2Json(scriptExecutor.execute(脚本_初始化)), Attribute.class);
                    monster.setAttribute(attribute);
                }
                final Attribute baseAttribute = attribute;
                for (int i = 0; i < monster.getNum(); i++) {//实例化怪物
                    Monster m = (Monster) monster.clone();
                    scriptExecutor.execute(脚本_初始化,baseAttribute);
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
                                    scriptExecutor.execute(脚本_初始化,baseAttribute);
                                    addM.setAttribute((Attribute) baseAttribute.clone());
                                    addM.setScript(monster.getScript());
                                    addM.setKey(BDate.getNowMills() + "m");
                                    Rooms.keySet().forEach(key ->
                                            {
                                                if(Rooms.get(key).get(m.getMap() + "")!=null)
                                                    Rooms.get(key).get(m.getMap() + "").add(addM);
                                            }
                                    );
                                    for (Client item : clients) {//发送房间东西新增
                                        try {
                                            if (item.getPlayer().getPlayerData().getRoom().getId()==m.getMap()) {
                                                item.sendMsg(msgBuild(Enum.messageType.normal, onObjectInRoom, object2JsonStr(addM.toSendGameObject()), null));
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                    scriptExecutor.execute(脚本_事件_复活事件,client,addM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start());
                    }
                    Rooms.keySet().forEach(key ->
                            {
                                if(Rooms.get(key).get(m.getMap() + "")!=null)
                                    Rooms.get(key).get(m.getMap() + "").add(m);
                            }
                    );
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
