package com.eztv.mud.admin.dao.impl;

import com.eztv.mud.GameUtil;
import com.eztv.mud.admin.config.DatabaseFactory;
import com.eztv.mud.admin.dao.MapDAO;
import com.eztv.mud.admin.model.TableSend;
import com.eztv.mud.admin.model.item.ObjectModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-31 10:31
 * 功能: 
 **/
@Service
public class MapImpl implements MapDAO {
    DatabaseFactory db;

    @Override
    public String getMapList(int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        List<ObjectModel> npcList = db.init().createSQL("" +
                "select * from t_map order by id desc limit ?,?",
                (page-1)*limit,limit).list(ObjectModel.class);
        tableSend.setCount(npcList.size());
        tableSend.setData(npcList);
        tableSend.setCode(0);
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public String getMapListByWhere(String value, int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        try{
            List<ObjectModel> npcList = db.init().createSQL("select * from t_map" +
                    " where name like '%"+value+"%' order by id desc limit ?,?",
                    (page-1)*limit,limit).list(ObjectModel.class);
            tableSend.setCount(npcList.size());
            tableSend.setData(npcList);
            tableSend.setCode(0);
        }catch(Exception e){}
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public boolean delMap(String id) {
        if(db.init().createSQL("delete from t_map where id = ?",id).update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean createMap() {
        if(db.init().createSQL("insert into t_map(createat) values(current_timestamp());").update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean saveMap(String id, String name) {
        if(db.init().createSQL(""+
                        "update t_map set " +
                        "name = ? " +
                        "where id = ?"
                ,name,id).update()>0){
            return true;
        }
        return false;
    }
}
