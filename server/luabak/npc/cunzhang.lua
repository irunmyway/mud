local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")
local 游戏工具 = luajava.bindClass("com.eztv.mud.GameUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
local lua工具;
local fun = _G[funName];
function 初始化()
    --初始属性
    属性:setHp(999);
    属性:setHp_max(999);
    属性:setMp(50);
    属性:setMp_max(50);
    属性:setExp(999);
    属性:setExp_max(999);
    属性:setAtk(10);
    return 属性:到Json();
end

function 对话(client, 窗口, 消息, 目标)
    lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
    if (消息:getRole() ~= nil) then
        if (消息:getRole() ~= '') then
            fun = _G[消息:getRole()]
            fun(client, 窗口, 消息, 目标);
            return ;
        end
    end
    lua工具:添加选项("你好!", "action", "", 消息:getMsg(), '');
    lua工具:添加选项("讨伐怪物","action", "doTalk", 消息:getMsg(), "任务1");
    lua工具:添加选项("对话谷中法师","action", "doTalk", 消息:getMsg(), "任务2");
    窗口:内容("你好啊年轻人");
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client,"action","doTalk",key,窗口);
end
--打怪任务
function 任务1(client, 窗口, 消息, gameObj)
    --任务创建部分
    local 任务 = luajava.newInstance("com.eztv.mud.LuaUtil");
    local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--奖励
    奖励:给经验(1350);--经验
    奖励:给物品(1, 1);--id 为1的物品给与1个
    任务:任务创建("kill1", "can", nil, "击杀两只怪物");
    任务:任务创建条件(1, 2);--id为1的怪物杀两只
    任务:任务添加条件集("kill");
    任务:任务设置奖励(奖励);
    --任务接受 逻辑 查看玩家是否接了任务，没有则添加，看是否完成
    local 该任务 =lua工具:检查任务状态(client, 任务:取任务());--我身上的任务查  看玩家是否接了任务，没有则添加，看是否完成
    if ("finished" == 该任务:取状态()) then
        窗口:内容("少侠做的不错！这是给你的奖励");
        lua工具:添加选项("小事一桩", "action", "doTalk", "", "testTask");
        lua工具:发送奖励(client, 奖励);
        该任务:setTaskState(Enum.taskState.cant);--设置为不可接 以后循环任务可以操作
    elseif ("processing" == 该任务:取状态() or "can" == 该任务:取状态()) then
        该任务:置状态("processing");
        窗口:内容(任务:getTask():getDesc() .. "\n" .. 该任务:取任务详情());
        lua工具:添加选项("我这就去把他们击溃", "action", "doTalk", "", "testTask");
    else--已经做完任务
        窗口:内容("少侠快去别的地方探索吧");
        lua工具:添加选项("谢谢", "action", "doTalk", "", "");
    end
    窗口:添加选项集合(lua工具);
    窗口:列数(2);
    lua工具:返回元素消息(client,"action","doTalk",消息:getRole(),窗口);
end
--对话任务
function 任务2(client, 窗口, 消息, gameObj)
    --任务创建部分
    local 任务 = luajava.newInstance("com.eztv.mud.LuaUtil");
    local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--奖励
    奖励:给经验(1350);--经验
    奖励:给物品(1, 1);--id 为1的物品给与1个
    任务:任务创建("testtalk", "can", nil, "我在村长不便,劳烦少侠替我去找谷中法师 问他是否愿意帮我们击败恶魔！");
    任务:任务创建条件(24, 1);--id为24的npc对话
    任务:任务添加条件集("talk");
    任务:任务设置奖励(奖励);
    --任务接受 逻辑 查看玩家是否接了任务，没有则添加，看是否完成
    local 该任务 =lua工具:检查任务状态(client, 任务:取任务());--我身上的任务查  看玩家是否接了任务，没有则添加，看是否完成
    if ("finished" == 该任务:取状态()) then
        窗口:内容("法师的事情算告一段落了");
        lua工具:添加选项("小事一桩", "action", "doTalk", "", "");
        lua工具:发送奖励(client, 奖励);
        该任务:setTaskState(Enum.taskState.cant);--设置为不可接 以后循环任务可以操作
    elseif ("processing" == 该任务:取状态() or "can" == 该任务:取状态()) then
        该任务:置状态("processing");
        窗口:内容(任务:getTask():getDesc() .. "\n" .. 该任务:取任务详情());
        lua工具:添加选项("义不容辞", "action", "doTalk", "", "");
    else--已经做完任务
        窗口:内容("少侠快去别的地方探索吧");
        lua工具:添加选项("谢谢", "action", "doTalk", "", "");
    end
    窗口:添加选项集合(lua工具);
    窗口:列数(2);
    lua工具:返回元素消息(client,"action","doTalk",消息:getRole(),窗口);
end
