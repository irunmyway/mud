package com.eztv.mud.handler.bean.commands.action;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.handler.MapHandler;
import com.eztv.mud.handler.bean.commands.BaseCommand;

import static com.eztv.mud.GameUtil.getAttribute;

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
        DataHandler. getPlayer(getClient(),getClient().getPlayer());
        MapHandler.getMapDetail(getClient());
        getAttribute(getClient());
    }
}
