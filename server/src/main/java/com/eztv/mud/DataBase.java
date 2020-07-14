package com.eztv.mud;

import lombok.val;
import online.sanen.cdm.api.Bootstrap;
import online.sanen.cdm.api.basic.DriverOption;
import online.sanen.cdm.core.factory.Bootstraps;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


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
            String src = System.getProperty("user.dir")+"/config.properties";
            Properties dbConfig = new Properties();
            try {
                dbConfig.load(new FileInputStream(src));
                _db = Bootstraps.newBuilder("")
                        .setUrl(dbConfig.getProperty("db_Url"))
                        .setDriver(DriverOption.MYSQL)
                        .setUsername(dbConfig.getProperty("db_Username"))
                        .setPassword(dbConfig.getProperty("db_Password"))
                        .setIsFormat(false).build();
            } catch (IOException e) {
                e.printStackTrace();
                _db = Bootstraps.newBuilder("")
                        .setUrl("jdbc:mysql://127.0.0.1:3306/mud?useSSL=false")
                        .setDriver(DriverOption.MYSQL)
                        .setUsername("root")
                        .setPassword("123456")
                        .setIsFormat(false).build();
            }

        }
        return _db;
    }
}
