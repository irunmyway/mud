package com.eztv.mud.command.commands.pop;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.Room;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.Constant.脚本_地图查看;
import static com.eztv.mud.GameUtil.getCurRoom;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-21 22:42
 * 功能: 脚本_地图查看
 **/
public class MapPanel extends BaseCommand {
    public MapPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        Room room = getCurRoom(getClient());
        String script = getCurRoom(getClient()).getScript();
        if(script==null)return;
        if(script.length()<1)return;
        getClient().getScriptExecutor().load(getCurRoom(getClient()).getScript()).
                execute(脚本_地图查看,getClient(),getWinMsg(),room);
    }
}
