package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.GamePublic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eztv.mud.Constant.GamePublicSql;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-09-04 23:15
 * 功能: 游戏变量存储
 **/
public class GameCache {
    public static Map<String,GamePublic> maps = new HashMap<>();

    public static void initGameCache() {
        synchronized (maps) {
            maps.clear();
            List<GamePublic> publics = DataBase.getInstance().init().createSQL(GamePublicSql).list(GamePublic.class);
            publics.forEach(gamePublic -> {
                maps.put(gamePublic.getId(),gamePublic);
            });
            publics = null;
        }
    }

    public synchronized static GamePublic get(String id) {
        return maps.get(id);
    }

    public synchronized static void set(String id,String data) {
        GamePublic gamePublic = maps.get(id);
        if(gamePublic==null){
            gamePublic = new GamePublic();
            gamePublic.setCreateat(new Date());
            gamePublic.setId(id);
            gamePublic.setData(data);
            maps.put(id,gamePublic);
            DataBase.getInstance().init().query(gamePublic).insert();
        }else{
            gamePublic.setData(data);
            gamePublic.setUpdateat(new Date());
            DataBase.getInstance().init().query(gamePublic).update();
        }

    }
}
