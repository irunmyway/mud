package com.eztv.mud.handler.event.player;

import com.alibaba.fastjson.JSONObject;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.Bag;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.bean.task.TaskAction;
import com.eztv.mud.bean.task.TaskCondition;
import com.eztv.mud.cache.PlayerCache;
import com.eztv.mud.constant.Enum;
import com.eztv.mud.handler.DataHandler;
import com.eztv.mud.syn.WordSyn;
import com.eztv.mud.utils.BDate;

import static com.eztv.mud.Constant.脚本_击杀奖励;
import static com.eztv.mud.GameUtil.*;
import static com.eztv.mud.constant.Cmd.onObjectOutRoom;
import static com.eztv.mud.handler.event.player.msg.DeadMsg.showPanel;

public class PlayerDead {
    public static void onDied(GameObject whoKill, GameObject diedObj, Client client) {
        //死亡回调
        if (diedObj instanceof Player) {
            showPanel(diedObj);
        }

        /**
         作者：hhx QQ1025334900
         日期: 2020-07-15 17:27
         用处：任务触发 查看玩家任务 并且计数
         **/
        for (Task task : client.getPlayer().getPlayerData().getTasks()) {
            if (task.getTaskState() == Enum.taskState.processing) {
                task.setTaskState(Enum.taskState.finished);
                for (TaskCondition taskCondition : task.getTaskConditions()) {
                    for (TaskAction taskAction : taskCondition.getTaskActions()) {
                        if (taskAction.getId().equals(diedObj.getId() + "")) {
                            if (taskAction.addProcess(1) < taskAction.getNum()) {
                                task.setTaskState(Enum.taskState.processing);
                            }
                        }
                    }
                }
            }
        }
        //记录死亡时间
        if (diedObj instanceof Player) {
            PlayerCache.getPlayer(((Player) diedObj).getAccount()).setDeadTime(BDate.getNowMills());
        }

        /**
         作者：hhx QQ1025334900
         日期: 2020-07-15 17:26
         用处：//移除玩家杀死的其他东西   奖励触发
         **/
        if (!(diedObj instanceof Player)) {//击杀奖励
            client.getScriptExecutor().load(diedObj.getScript() + ".lua");
            Bag reward = JSONObject.toJavaObject(jsonStr2Json(client.getScriptExecutor().execute(脚本_击杀奖励).toString()), Bag.class);
            DataHandler.sendReward(client, client.getPlayer().getPlayerData().toReward(reward));
        }

        //移除死亡的东西
        if (!(diedObj instanceof Player)) {
            WordSyn.InOutRoom(diedObj, ((Player) whoKill).getPlayerData().getRoom(), false);
            GameUtil.sendToAll(msgBuild(Enum.messageType.normal, onObjectOutRoom, object2JsonStr(diedObj), null));
        }

        /**
         作者：hhx QQ1025334900
         日期: 2020-07-15 17:26
         用处：怪物刷新模块触发
         **/
        if (diedObj.getRefreshment() == 0) return;
        diedObj.getiGameObject().onRefresh(client);


    }
}
