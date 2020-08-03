package com.eztv.mud.admin.dao.impl;

import com.eztv.mud.GameUtil;
import com.eztv.mud.admin.config.DatabaseFactory;
import com.eztv.mud.admin.dao.ItemDAO;
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
public class ItemImpl implements ItemDAO {
    DatabaseFactory db;


    @Override
    public String getItemList(int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        List<ObjectModel> npcList = db.init().createSQL("" +
                "select * from t_item order by createat desc limit ?,?" +
                "",(page-1)*limit,limit).list(ObjectModel.class);
        tableSend.setCount(npcList.size());
        tableSend.setData(npcList);
        tableSend.setCode(0);
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public String getItemListByWhere(String value, int page, int limit) {
        TableSend tableSend = new TableSend();
        tableSend.setCode(1);
        List<ObjectModel> npcList = db.init().createSQL("select * from t_item" +
                " where name like '%"+value+"%' limit ?,?",(page-1)*limit,limit).list(ObjectModel.class);
        tableSend.setCount(npcList.size());
        tableSend.setData(npcList);
        tableSend.setCode(0);
        return GameUtil.object2JsonStr(tableSend);
    }

    @Override
    public boolean delItem(String id) {
        if(db.init().createSQL("delete from t_item where id = ?",id).update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean createItem() {
        if(db.init().createSQL("insert into t_item(createat) values(current_timestamp());").update()>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean saveItem(String id, String name, String script) {
        if(db.init().createSQL(""+
                        "update t_item set " +
                        "name = ?, " +
                        "script=? " +
                        "where id = ?"
                ,name,script,id).update()>0){
            return true;
        }
        return false;
    }
}
