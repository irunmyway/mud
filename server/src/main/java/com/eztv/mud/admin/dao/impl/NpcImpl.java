package com.eztv.mud.admin.dao.impl;

import com.eztv.mud.GameUtil;
import com.eztv.mud.admin.config.DatabaseFactory;
import com.eztv.mud.admin.dao.NpcDAO;
import com.eztv.mud.admin.model.item.ObjectModel;
import com.eztv.mud.admin.model.TableSend;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-31 10:31
 * 功能: 
 **/
@Service
public class NpcImpl implements NpcDAO {
    DatabaseFactory db;
    @Override
    public String getNpcList(int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        List<ObjectModel> npcList = db.init().createSQL("" +
                "select t1.*,t2.name mapName from t_npc t1 left join t_map t2 " +
                "on t1.map = t2.id order by createat desc limit ?,?" +
                "",(page-1)*limit,limit).list(ObjectModel.class);
        tableSend.setCount(npcList.size());
        tableSend.setData(npcList);
        tableSend.setCode(0);
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public String getNpcListByWhere(String value, int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        List<ObjectModel> npcList = db.init().createSQL("SELECT * from (\n" +
                "select t1.*,t2.name mapName from t_npc t1 left JOIN  t_map t2 on \n" +
                "t1.map = t2.id\n" +
                ")t3 where name like '%"+value+"%' or mapName like '%"+value+"%' limit ?,?"
        ,(page-1)*limit,limit).list(ObjectModel.class);
        tableSend.setCount(npcList.size());
        tableSend.setData(npcList);
        tableSend.setCode(0);
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public boolean delNpc(String id) {
        if(db.init().createSQL("delete from t_npc where id = ?",id).update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean createNpc() {
        if(db.init().createSQL("insert into t_npc(createat) values(current_timestamp());").update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean saveNpc(String id, String name, String desc, String map, String script, String num) {
        if(db.init().createSQL(""+
                "update t_npc set " +
                        "name = ?, " +
                        "`desc` = ?," +
                        "map=?," +
                        "script=?," +
                        "num=?"+
                "where id = ?"
        ,name,desc,map,script,num,id).update()>0){
            return true;
        }
        return false;
    }


}
