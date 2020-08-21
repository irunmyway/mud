package com.eztv.mud.command.commands.action;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.cache.manager.ClientManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

/**
 作者：hhx QQ1025334900
 日期: 2020-07-22 10:50
 用处：死亡后的 预复活 处理
**/
public class ReliveAction extends BaseCommand {
    public ReliveAction(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        long waitTime =  ClientManager.isDead(getClient(),getPlayer());
        if(waitTime>0){
            getMsg().setType(Enum.messageType.toast);
            getMsg().setMsg("还要"+(waitTime)+"秒才能复活");
            getClient().sendMsg(JSONObject.parseObject(JSONObject.toJSON(getMsg()).toString()).toJSONString());
        }
    }
}
