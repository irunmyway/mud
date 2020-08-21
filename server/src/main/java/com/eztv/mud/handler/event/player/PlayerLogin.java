package com.eztv.mud.handler.event.player;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.net.WinMessage;

import static com.eztv.mud.Constant.脚本_初始化;
import static com.eztv.mud.Constant.Other_PATH;

public class PlayerLogin {
    //登录事件
    public static void onLogin(Client client){
        client.getScriptExecutor().load(
                Other_PATH+"login")
                .execute(脚本_初始化,client,new WinMessage());
    }


}
