package com.eztv.mud.command.commands.relation;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.manager.ClientManager;
import com.eztv.mud.command.commands.BaseCommand;

import static com.eztv.mud.Constant.Init_PATH;
import static com.eztv.mud.Constant.脚本_初始化;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-28 16:54
 * 功能: 敌人追踪
 **/
public class EnemyTrace extends BaseCommand {
    public EnemyTrace(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        String playerKey = getMsg().getMsg();
        Player target=null;
        if(playerKey!=null){
            target = ClientManager.getClient(playerKey).getPlayer();
        }
        getClient().getScriptExecutor().load(Init_PATH+"enemytrace").
                execute(脚本_初始化,getClient(),target,getWinMsg(),new Msg());
    }
}
