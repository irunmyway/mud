package com.eztv.mud;

import com.eztv.mud.utils.BProp;
import online.sanen.cdm.api.Bootstrap;
import online.sanen.cdm.api.basic.DriverOption;
import online.sanen.cdm.core.factory.Bootstraps;

import java.util.Properties;


public class DataBase {
    private static DataBase Instance;
    private static Bootstrap _db;

    public DataBase() {
        //开定时存储玩家数据
        //saveHandler();
    }

    //获取单例
    public static DataBase getInstance() {
        if (Instance == null) {
            Instance = new DataBase();
        }
        return Instance;
    }

    public Bootstrap init(){
        if (_db == null) {
            //初始化配置文件
            BProp.getInstance().load(System.getProperty("user.dir")+"/config.properties");
            Properties dbConfig = BProp.getInstance().getProp();
            _db = Bootstraps.newBuilder("")
                    .setUrl(dbConfig.getProperty("db_Url"))
                    .setDriver(DriverOption.MYSQL)
                    .setUsername(dbConfig.getProperty("db_Username"))
                    .setPassword(dbConfig.getProperty("db_Password"))
                    .setIsFormat(false).build();

        }
        return _db;
    }
}
