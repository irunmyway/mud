package com.eztv.mud.handler.event;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.net.WinMessage;

import static com.eztv.mud.Constant.LUA_初始化;
import static com.eztv.mud.Constant.Other_PATH;

public class PlayerEvent {
    //登录事件
    public static void onLogin(Client client){
        client.getScriptExecutor().loadFile(null,
                Other_PATH+"login")
                .execute(LUA_初始化,client,new WinMessage());
    }


}
