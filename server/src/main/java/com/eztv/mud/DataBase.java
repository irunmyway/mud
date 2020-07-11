package com.eztv.mud;

import online.sanen.cdm.api.Bootstrap;
import online.sanen.cdm.api.basic.Configuration;
import online.sanen.cdm.api.basic.DriverOption;
import online.sanen.cdm.core.factory.Bootstraps;

public class DataBase {
    private static DataBase Instance;
    private static Bootstrap _db;
    //获取单例
    public static DataBase getInstance() {
        if (Instance == null) {
            Instance = new DataBase();
        }
        return Instance;
    }

    public Bootstrap init(){
        if (_db == null) {
            _db = Bootstraps.newBuilder("")
                    .setUrl("jdbc:mysql://127.0.0.1:3306/mud?useSSL=false")
                    .setDriver(DriverOption.MYSQL)
                    .setUsername("root")
                    .setPassword("123456")
                    .setIsFormat(false).build();
        }
        return _db;
    }
}
