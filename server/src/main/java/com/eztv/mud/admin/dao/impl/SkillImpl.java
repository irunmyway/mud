package com.eztv.mud.admin.dao.impl;

import com.eztv.mud.GameUtil;
import com.eztv.mud.admin.config.DatabaseFactory;
import com.eztv.mud.admin.dao.SkillDAO;
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
public class SkillImpl implements SkillDAO {
    DatabaseFactory db;

    @Override
    public String getSkillList(int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        List<ObjectModel> npcList = db.init().createSQL("" +
                "select * from t_skill order by createat desc limit ?,?" +
                "",(page-1)*limit,limit).list(ObjectModel.class);
        tableSend.setCount(npcList.size());
        tableSend.setData(npcList);
        tableSend.setCode(0);
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public String getSkillListByWhere(String value, int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        List<ObjectModel> npcList = db.init().createSQL("select * from t_skill" +
                " where name like '%"+value+"%' limit ?,?",(page-1)*limit,limit).list(ObjectModel.class);
        tableSend.setCount(npcList.size());
        tableSend.setData(npcList);
        tableSend.setCode(0);
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public boolean delSkill(String id) {
        if(db.init().createSQL("delete from t_skill where id = ?",id).update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean createSkill() {
        if(db.init().createSQL("insert into t_skill(createat) values(current_timestamp());").update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean saveSkill(String id, String name, String script) {
        if(db.init().createSQL(""+
                        "update t_skill set " +
                        "name = ?, " +
                        "script=?" +
                        "where id = ?"
                ,name,script,id).update()>0){
            return true;
        }
        return false;
    }
}
