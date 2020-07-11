package com.eztv.mud;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.Enum;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.utils.BDate;
import com.eztv.mud.utils.BDebug;
import com.eztv.mud.utils.BFile;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.*;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.bean.Cmd.onObjectInRoom;

public class Word {
    private HashMap<String, Room> Rooms = new HashMap<String, Room>();
    private HashMap<String, LuaValue> npcScript = new HashMap<String, LuaValue>();
    private HashMap<String, LuaValue> monsterScript = new HashMap<String, LuaValue>();
    private HashMap<String, Attribute> baseAttributes = new HashMap<String, Attribute>();
    private Globals globals = JsePlatform.standardGlobals();
    private String GG = "";
    private static Word Instance;

    //获取单例
    public static Word getInstance() {
        if (Instance == null) {
            Instance = new Word();
        }
        return Instance;
    }

    private void initGG() {//加载公告
//        String src = this.getClass().getClassLoader().getResource("gg").getPath();
//        GG = BFile.readFromFile(src);
        InputStream is = this.getClass().getResourceAsStream("/gg");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = "";
        try {
            while ((s = br.readLine()) != null)
                GG += s;
        } catch (Exception e) {
        }
        BDebug.trace("公告加载完成");
    }

    private void initRooms() {//初始化所有房间
        //初始化所有房间
        Rooms.clear();
        List<Room> roomList = DataBase.getInstance().init().createSQL("select * from t_map").list(Room.class);
        for (Room room : roomList) {
            try {
                Rooms.put(room.getId() + "", room);
            } catch (Exception e) {
            }
        }
        BDebug.trace("地图加载完成 数量 : Map load mapNum:【" + Rooms.size() + "】");
    }

    private void initNPC() {//加载所有npc 和他的脚本
        npcScript.clear();
        List<Npc> npcList = DataBase.getInstance().init().createSQL("select * from t_npc").list(Npc.class);
        for (Npc npc : npcList) {
            try {
                Attribute attribute = new Attribute();
                if (npc.getScript().length() > 0) {
                    npc.setScript(NPC_PATH + npc.getScript());
                    npcScript.put(npc.getId() + "", globals.loadfile(npc.getScript() + ".lua"));
                    globals.load(npcScript.get(npc.getId() + ""));
                    attribute = JSONObject.toJavaObject(jsonStr2Json(globals.get(LuaValue.valueOf("init")).invoke().toString()), Attribute.class);
                    npc.setAttribute(attribute);
                }
                final Attribute baseAttribute = attribute;

                for (int i = 0; i < npc.getNum(); i++) {//实例NPC
                    Npc m = (Npc) npc.clone();
                    m.setAttribute((Attribute) baseAttribute.clone());
                    m.setScript(npc.getScript());
                    m.setKey(BDate.getNowMills() + i + "");
                    if (m.getRefreshment() > 0) {
                        m.setiGameObject((client) -> new Thread(new Runnable() {//NPC复活回调
                            @Override
                            public void run() {
                                GameObject addM;
                                try {
                                    Thread.sleep(m.getRefreshment());
                                    addM = (Npc) m.clone();
                                    addM.setAttribute((Attribute) baseAttribute.clone());
                                    addM.setScript(npc.getScript());
                                    addM.setKey(BDate.getNowMills() + "");
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
                    Rooms.get(npc.getMap() + "").add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BDebug.trace("NPC加载完成 数量 : Npc load npc_num:【" + npcScript.size() + "】");
    }

    private void initMonster() {//加载所有怪物 和他的脚本
        monsterScript.clear();
        List<Monster> monsters = DataBase.getInstance().init().createSQL("select * from t_monster").list(Monster.class);
        for (Monster monster : monsters) {
            try {
                Attribute attribute = new Attribute();
                //List<Choice> choice = new ArrayList<>();
                if (monster.getScript().length() > 0) {//模板
                    monster.setScript(Monster_PATH + monster.getScript());
                    monsterScript.put(monster.getId() + "", globals.loadfile(monster.getScript() + ".lua"));
                    globals.load(monsterScript.get(monster.getId() + ""));
                    attribute = JSONObject.toJavaObject(jsonStr2Json(globals.get(LuaValue.valueOf("init")).invoke().toString()), Attribute.class);
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

    private void initBaseAttribute() {
        //加载属性上线 基础公式表
        baseAttributes.clear();
        List<Attribute> attributeList = DataBase.getInstance().init().createSQL("select * from t_attribute").list(Attribute.class);
        for (Attribute attribute : attributeList) {
            baseAttributes.put(attribute.getLevel() + "", attribute);
        }
        BDebug.trace("基础属性加载完成 最高等级 : Attribute load max_level:【" + baseAttributes.size() + "】");
    }

    public void init() {
        initGG();
        initRooms();
        initNPC();
        initMonster();
        initBaseAttribute();
    }

    public HashMap<String, Room> getRooms() {
        return Rooms;
    }

    public HashMap<String, Attribute> getBaseAttributes() {
        return baseAttributes;
    }

    public HashMap<String, LuaValue> getMonsterScript() {
        return monsterScript;
    }

    public String getGG() {
        return GG;
    }
}
