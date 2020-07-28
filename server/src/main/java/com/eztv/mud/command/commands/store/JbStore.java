package com.eztv.mud.command.commands.store;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;
import org.luaj.vm2.LuaValue;

import static com.eztv.mud.Constant.LUA_初始化;
import static com.eztv.mud.Constant.Store_PATH;

public class JbStore extends BaseCommand {
    public JbStore(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        LuaValue luaValue = getClient().getScriptExecutor().loadFile(null,
                Store_PATH+"jbstore")
                .execute(LUA_初始化,getClient(),new WinMessage(),getMsg());
    }
}
