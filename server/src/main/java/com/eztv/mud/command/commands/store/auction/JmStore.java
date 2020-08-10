package com.eztv.mud.command.commands.store.auction;

import com.eztv.mud.Constant;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;

public class JmStore extends BaseCommand {
    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-08-07 21:11
     * 功能: 查看寄卖面板
     **/
    public JmStore(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        getClient().getScriptExecutor().loadFile(null, Constant.Store_PATH+"jmstore"
                ).execute(Constant.LUA_初始化,getClient(),new WinMessage(),getMsg());
    }
}
