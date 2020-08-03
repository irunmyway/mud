package com.eztv.mud.admin.config;

import com.eztv.mud.DataBase;
import online.sanen.cdm.api.Bootstrap;
import org.springframework.stereotype.Service;

@Service
public class DatabaseFactory {
    static DatabaseFactory instance;
    public static Bootstrap getInstance(){
        if (instance == null)instance = new DatabaseFactory();
        return instance.init();
    }
    public static Bootstrap init(){
        return DataBase.getInstance().init();
    }
}
