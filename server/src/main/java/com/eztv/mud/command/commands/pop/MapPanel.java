package com.eztv.mud.command.commands.pop;

import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.Room;
import com.eztv.mud.bean.net.WinMessage;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.List;

import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.doTalk;

public class MapPanel extends BaseCommand {
    public MapPanel(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        WinMessage winMsg = new WinMessage();
        List<Choice> choice = new ArrayList<>();
        Room room = getCurRoom(getClient());
        //执行下当前地图的脚本 获取下信息
        winMsg.setDesc(getProp("map_panel",
                room.getName(),
                room.getDesc(),
                "一朵小红花"
                ));

        winMsg.setChoice(choice);
        getClient().sendMsg(msgBuild(
                Enum.messageType.pop,
                doTalk,
                object2JsonStr(winMsg),
                "").getBytes());
    }
}
