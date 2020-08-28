package com.eztv.mud.script;

import com.eztv.mud.LuaUtil;
import com.eztv.mud.bean.*;
import com.eztv.mud.bean.net.Player;
import com.eztv.mud.bean.task.Task;

import java.util.List;

public class LuaOpen {


    public interface LuaAction {

        //玩家操作
        public boolean 学习技能(Client client, Item item);

        public String 装备(Client client, Item item);//装备上当前物品

        public int 取等级(Client client);


        //任务部分
        public LuaUtil 任务创建(String id, String taskState, String nextId, String desc);//任务状态

        Task 取任务();

        String 取任务状态(Client client, String taskId);

        public void 任务设置奖励(Bag reward);

        public LuaUtil 任务创建条件(String id, int num);//添加具体任务

        public LuaUtil 任务添加条件集(String taskType);//添加一个任务任务类型

        MsgMap 到消息(String str);

        List<Item> 取背包物品集合(Client client);

        //通信部分
        void 发送消息(Client client, byte[] msg);

        void 返回系统消息(Client client, String str);

        void 全局系统消息(String str);

        void 返回数组消息(Client client, String messageType, String cmd, String key, Object obj);

        void 返回元素消息(Client client, String messageType, String cmd, String key, Object obj);


        long 取离线时间(Client client);

        void 挂机奖励(Client client);

        void 购买(Client client, String id, String num, long price);

        void 购买技能(Client client, String id, String num, long price);

        void 元宝购买(Client client, String id, String num, long price);

        void 元宝购买技能(Client client, String id, String num, long price);
    }

    public interface LuaMap {
        void 到房间(Client client, String map, String id);
        void 到帮派房间(Client client, String map, String id);
        Room 取房间(Player player);
    }

    public interface LuaMath {
        double 取随机数(int a, int b);
    }

    public interface LuaWin {
        void 添加选项集合(LuaUtil luaUtil);

        void 内容(String string);

        void 列数(int col);
    }

    public interface LuaItem {
        void 类型(String type);

        void 属性(Attribute attribute);

        void 内容(String string);

        String 到文本(String type);
    }

    public interface LuaFaction {
        int 取帮派地图(Client client);
    }

    public interface LuaTask {
        String 取任务详情();

        String 取状态();

        void 置状态(String state);
    }

    public interface LuaBag {
        void 给物品(int id, int num);

        void 给技能(int id, int num);

        void 删物品(int id, int num);

        void 删物品集(int id);

        void 给经验(long exp);

        void 给铜币(long money);

        void 给金币(long jb);

        void 给元宝(long yb);

        String 到文本();

    }

    public interface LuaJson {
        String 到Json();
    }

    public interface LuaChoice {
        Choice 背景颜色(String colo);
    }

}