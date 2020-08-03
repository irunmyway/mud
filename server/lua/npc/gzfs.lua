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
    return 游戏工具:object2JsonStr(属性);
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
    lua工具:添加选项("传送阵","action", "a", 消息:getMsg(), "任务1", true);
    窗口:内容("从这里踏上新的征程<br>我可以带你去这些地方...");
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client,"action","doTalk",key,窗口);
end

function 任务1(client, 窗口, 消息, gameObj)
    --任务创建部分
    local 任务 = luajava.newInstance("com.eztv.mud.LuaUtil");
    local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--奖励
    奖励:给经验(1350);--经验
    奖励:给物品(1, 1);--id 为1的物品给与1个
    任务:任务创建("kill1", nil, nil, "击杀两只怪物");
    任务:任务创建条件(1, 2);--id为1的怪物杀两只
    任务:任务添加条件集("kill");
    任务:任务设置奖励(奖励);
    --任务接受 逻辑 查看玩家是否接了任务，没有则添加，看是否完成
    local 该任务 =lua工具:检查任务状态(client, 任务:取任务());--我身上的任务查  看玩家是否接了任务，没有则添加，看是否完成
    if ("finished" == 该任务:取状态()) then
        窗口:内容("少侠做的不错！这是给你的奖励");
        lua工具:添加选项("小事一桩", "action", "doTalk", "", "testTask", true);
        lua工具:发送奖励(client, 奖励);
        该任务:setTaskState(Enum.taskState.cant);--设置为不可接 以后循环任务可以操作
    elseif ("processing" == 该任务:取状态()) then
        窗口:内容(任务:getTask():getDesc() .. "\n" .. 该任务:取任务详情());
        lua工具:添加选项("我这就去把他们击溃", "action", "doTalk", "", "testTask", true);
    else--已经做完任务
        窗口:内容("少侠快去别的地方探索吧");
        lua工具:添加选项("谢谢", "action", "doTalk", "", "", true);
    end
    窗口:添加选项集合(lua工具);
    窗口:列数(2);
    lua工具:返回元素消息(client,"action","doTalk",消息:getRole(),窗口);
end
