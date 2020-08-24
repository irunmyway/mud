package com.eztv.mud.command.commands.relation;

import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Chat;
import com.eztv.mud.bean.Choice;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.cache.RelationCache;
import com.eztv.mud.cache.manager.ClientManager;
import com.eztv.mud.cache.manager.RelationManager;
import com.eztv.mud.command.commands.BaseCommand;
import com.eztv.mud.constant.Enum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.GameUtil.*;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-24 17:53
 * 功能: 结交好友
 **/
public class FriendMake extends BaseCommand {
    public FriendMake(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        if (getMsg().getRole() != null && getMsg().getMsg() != null) {//确定同意
            if (getMsg().getRole().equals("do")) {
                boolean allow;
                try {
                    allow = RelationCache.allows.get(getMsg().getMsg()).get(getPlayer().getKey());
                } catch (Exception e) {
                    allow = false;
                }
                if (allow) {
                    //新建关系 然后保存到缓存里
                    Player target = ClientManager.getClient(getMsg().getMsg()).getPlayer();
                    boolean flag = RelationManager.makeFriend(getPlayer().getAccount(), target);
                    String send;
                    if (flag) {//添加成功
                        send = getPropByFile("relation", "friend_success");
                    } else {//已经是好友
                        send = getPropByFile("relation", "friend_exit");
                    }
                    Chat chat = Chat.system(send);
                    GameUtil.sendToKey(getMsg().getMsg(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));
                    GameUtil.sendToKey(getPlayer().getKey(), msgBuild(Enum.messageType.chat, "公聊", object2JsonStr(chat), ""));

                }
                return;
            }
        }

        HashMap allow = new HashMap<>();
        allow.put(getMsg().getMsg(), true);
        RelationCache.allows.put(getPlayer().getKey(), allow);
        List<Choice> choice = new ArrayList<>();
        getWinMsg().setDesc(getPropByFile("relation", "friend_invite", getPlayer().getName()));
        choice.add(Choice.createChoice("同意好友请求", Enum.messageType.action, getMsg().getCmd(), getPlayer().getKey(), "do", Enum.winAction.close).setBgColor(Enum.color.blue));
        getWinMsg().setCol(2);
        getWinMsg().setChoice(choice);
        GameUtil.sendToKey(getMsg().getMsg(), msgBuild(Enum.messageType.pop, "", object2JsonStr(getWinMsg()), getMsg().getMsg(), null));

    }
}
