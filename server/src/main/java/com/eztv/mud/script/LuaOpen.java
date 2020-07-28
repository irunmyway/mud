package com.eztv.mud.script;

import com.eztv.mud.LuaUtil;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Bag;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.task.Task;

public class LuaOpen {


    public interface LuaAction {

        //玩家操作
        public boolean 学习技能(Client client, Item item);

        public String 装备(Client client, Item item);//装备上当前物品

        public int 当前等级(Client client);


        //任务部分
        public LuaUtil 任务创建(String id, String taskState, String nextId, String desc);//任务状态

        public void 任务设置奖励(Bag reward);

        public LuaUtil 任务创建条件(String id, int num);//添加具体任务

        public LuaUtil 任务添加条件集(String taskType);//添加一个任务任务类型


        //通信部分
        void 发送消息(Client client, byte[] msg);

        void 返回系统消息(Client client, String str);

        void 返回数组消息(Client client, String messageType, String cmd, String key, Object obj);

        void 返回元素消息(Client client, String messageType, String cmd, String key, Object obj);

        Task 取任务();
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

    public interface LuaTask {
        String 取任务详情();
        String 取状态();
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

    }

}