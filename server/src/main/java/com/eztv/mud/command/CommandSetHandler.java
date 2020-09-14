package com.eztv.mud.command;

import com.eztv.mud.command.commands.MinePanel;
import com.eztv.mud.command.commands.action.*;
import com.eztv.mud.command.commands.bag.BagPanel;
import com.eztv.mud.command.commands.equip.EquipPanel;
import com.eztv.mud.command.commands.faction.*;
import com.eztv.mud.command.commands.player.Attack;
import com.eztv.mud.command.commands.player.Talk;
import com.eztv.mud.command.commands.pop.MapPanel;
import com.eztv.mud.command.commands.pop.SkillAttackPanel;
import com.eztv.mud.command.commands.skill.SkillPanel;
import com.eztv.mud.command.commands.pop.TradePanel;
import com.eztv.mud.command.commands.relation.*;
import com.eztv.mud.command.commands.store.JbStore;
import com.eztv.mud.command.commands.store.YbStore;
import com.eztv.mud.command.commands.store.auction.*;

import java.util.HashMap;
import java.util.Map;

import static com.eztv.mud.constant.Cmd.doAttack;
import static com.eztv.mud.constant.Cmd.doTalk;

public class CommandSetHandler {
    //游戏指令集合
    public static Map<String, Class> actionCommandSet = new HashMap<>();
    //游戏面板集合
    public static Map<String, Class> panelCommandSet = new HashMap<>();

    public static void initActionCommandSet(){//问题：重新装备武器后 经验和当前血量重置了。。。
        actionCommandSet.put("item_action", ItemAction.class);//包括物品的一些操作
        actionCommandSet.put("skill_action", SkillAction.class);//包括技能的一些操作
        actionCommandSet.put("relive", ReliveAction.class);//玩家预复活处理
        actionCommandSet.put("joinFaction", JoinFaction.class);//加入门派
        actionCommandSet.put("createFaction", CreateFaction.class);//创立门派
        actionCommandSet.put("destroyFaction", DestroyFaction.class);//门派 面板
        actionCommandSet.put("recruitFaction", RecruitFaction.class);//招募
        actionCommandSet.put("exitFaction", ExitFaction.class);//退出帮派
        actionCommandSet.put("useSkill", UseSkill.class);//使用技能
        actionCommandSet.put("jbStore", JbStore.class);//寄卖商城
        actionCommandSet.put("ybStore", YbStore.class);//元宝商城
        actionCommandSet.put("jmStore", JmStore.class);//寄卖商城
        actionCommandSet.put("jmSell", JmSell.class);//寄卖商城
        actionCommandSet.put("jmSellCurrency", JmStore.class);//寄卖商城
        actionCommandSet.put("jmLook", JmLook.class);//寄卖查看
        actionCommandSet.put("jmCancel", JmCancel.class);//下架寄卖
        actionCommandSet.put("jmBuy", JmBuy.class);//寄卖购买
        actionCommandSet.put("jmReward", JmReward.class);//寄卖收益
        actionCommandSet.put(doTalk, Talk.class);//玩家对话
        actionCommandSet.put(doAttack, Attack.class);//玩家攻击
        actionCommandSet.put("makeFriend", FriendMake.class);//寄卖收益
        actionCommandSet.put("friendTalk", FriendTalk.class);//
        actionCommandSet.put("friendBreak", FriendBreak.class);//
        actionCommandSet.put("enemyBreak", EnemyBreak.class);//删除仇人
        actionCommandSet.put("enemyTalk", EnemyTalk.class);//仇人对话
        actionCommandSet.put("enemyTrace", EnemyTrace.class);//仇人追踪


    }

    public static void initPanelCommandSet(){
        panelCommandSet.put("my_equip", EquipPanel.class);//装备面板
        panelCommandSet.put("my_skills", SkillPanel.class);//技能面板

        panelCommandSet.put("useClick", UsePanel.class);//物品使用面板
        panelCommandSet.put("getBag", BagPanel.class);//背包面板
        panelCommandSet.put("getMap", MapPanel.class);//背包面板
        panelCommandSet.put("getMine", MinePanel.class);//我的 面板
        panelCommandSet.put("factionPanel", FactionPanel.class);//门派 面板
        panelCommandSet.put("confirmPanel", ConfirmPanel.class);//确认 面板
        panelCommandSet.put("getSkill", SkillAttackPanel.class);//技能 面板
        panelCommandSet.put("getTrade", TradePanel.class);//交易 面板
        panelCommandSet.put("memberFaction", MemberFaction.class);//
        panelCommandSet.put("grantMemberFaction", GrantMemberFaction.class);//
        panelCommandSet.put("relationPanel", RelationPanel.class);//门派 面板

        panelCommandSet.put("好友列表", FriendPanel.class);//门派 面板
        panelCommandSet.put("仇人列表", EnemyPanel.class);//门派 面板


    }


}
