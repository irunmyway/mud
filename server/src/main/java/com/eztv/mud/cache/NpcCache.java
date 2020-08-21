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

public class NpcCache {
    private static HashMap<String, String> npcScript = new HashMap<String, String>();

    public static void initNPC(ScriptExecutor scriptExecutor, HashMap<String ,HashMap<String, Room>> Rooms) {//加载所有npc 和他的脚本
        npcScript.clear();
        List<Npc> npcList = DataBase.getInstance().init().createSQL("select * from t_npc").list(Npc.class);
        for (Npc npc : npcList) {
            try {
                Attribute attribute = new Attribute();
                if (npc.getScript().length() > 0) {
                    npc.setScript(NPC_PATH + npc.getScript());
                    npcScript.put(npc.getId() + "", npc.getScript() );
                    scriptExecutor.load(npcScript.get(npc.getId() + ""));
                    scriptExecutor.execute(脚本_初始化,attribute);
//                    attribute = JSONObject.toJavaObject(jsonStr2Json(scriptExecutor.execute(脚本_初始化)), Attribute.class);
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
        BDebug.trace("NPC加载完成 数量 : Npc load npc_num:【" + npcScript.size() + "】");
    }
}
