package com.eztv.mud.command;

import com.eztv.mud.command.commands.MinePanel;
import com.eztv.mud.command.commands.action.ConfirmPanel;
import com.eztv.mud.command.commands.action.ItemAction;
import com.eztv.mud.command.commands.action.ReliveAction;
import com.eztv.mud.command.commands.action.UsePanel;
import com.eztv.mud.command.commands.bag.BagPanel;
import com.eztv.mud.command.commands.equip.EquipPanel;
import com.eztv.mud.command.commands.faction.CreateFaction;
import com.eztv.mud.command.commands.faction.DestroyFaction;
import com.eztv.mud.command.commands.faction.FactionPanel;
import com.eztv.mud.command.commands.faction.JoinFaction;

import java.util.HashMap;
import java.util.Map;

public class CommandSetHandler {
    //游戏指令集合
    public static Map<String, Class> actionCommandSet = new HashMap<>();
    //游戏面板集合
    public static Map<String, Class> panelCommandSet = new HashMap<>();

    public static void initActionCommandSet(){//问题：重新装备武器后 经验和当前血量重置了。。。
        actionCommandSet.put("item_action", ItemAction.class);//包括物品的一些操作
        actionCommandSet.put("relive", ReliveAction.class);//玩家预复活处理
        actionCommandSet.put("joinFaction", JoinFaction.class);//加入门派
        actionCommandSet.put("createFaction", CreateFaction.class);//创立门派
        actionCommandSet.put("destroyFaction", DestroyFaction.class);//门派 面板

    }

    public static void initPanelCommandSet(){
        panelCommandSet.put("my_equip", EquipPanel.class);//装备面板
        panelCommandSet.put("useClick", UsePanel.class);//物品使用面板
        panelCommandSet.put("getBag", BagPanel.class);//背包面板
        panelCommandSet.put("getMine", MinePanel.class);//我的 面板
        panelCommandSet.put("factionPanel", FactionPanel.class);//门派 面板
        panelCommandSet.put("confirmPanel", ConfirmPanel.class);//确认 面板


    }


}
