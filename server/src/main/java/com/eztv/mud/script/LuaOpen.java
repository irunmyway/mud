package com.eztv.mud.script;

import com.eztv.mud.LuaUtil;
import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Bag;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Item;
import com.eztv.mud.bean.task.Task;
import com.eztv.mud.constant.Enum;

public class LuaOpen {


    public interface LuaAction {

        //玩家操作
        public boolean 学习技能(Client client, Item item);

        public String 装备(Client client, Item item);//装备上当前物品

        public int 当前等级(Client client);

        //选项创建

        //任务部分
        public LuaUtil 创建任务(String id, Enum.taskState taskState, String nextId, String desc);//任务状态

        public void 设置奖励(Bag reward);

        public LuaUtil 创建任务条件(String id, int num);//添加具体任务

        public LuaUtil 添加条件到任务(String taskType);//添加一个任务任务类型


        //通信部分
        void 发送消息(Client client, byte[] msg);

        void 自己系统消息(Client client, String str);

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
        String 任务详情();
        String 状态();
    }

}