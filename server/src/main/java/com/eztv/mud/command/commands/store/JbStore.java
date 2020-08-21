package com.eztv.mud.command.commands.store;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;

import static com.eztv.mud.Constant.脚本_初始化;
import static com.eztv.mud.Constant.Store_PATH;

public class JbStore extends BaseCommand {
    public JbStore(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
       getClient().getScriptExecutor().load(
                Store_PATH+"jbstore")
                .execute(脚本_初始化,getClient(),new WinMessage(),getMsg());
    }
}
