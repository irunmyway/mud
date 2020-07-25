local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")
local 游戏工具 = luajava.bindClass("com.eztv.mud.GameUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
local luaUtil;
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
    luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
    if (消息:getRole() ~= nil) then
        if (消息:getRole() ~= '') then
            fun = _G[消息:getRole()]
            fun(client, 窗口, 消息, 目标);
            return ;
        end
    end
    luaUtil:添加选项("你好!", Enum.messageType.action, "", 消息:getMsg(), '', true);
    luaUtil:添加选项("讨伐怪物", Enum.messageType.action, "doTalk", 消息:getMsg(), "任务1", true);
    窗口:setDesc("你好啊年轻人");
    窗口:添加选项集合(luaUtil);
    client:sendMsg(游戏工具:msgBuildForBytes(Enum.messageType.action, "doTalk", 游戏工具:object2JsonStr(窗口), key));
    luaUtil = nil;
end

function 任务1(client, 窗口, 消息, gameObj)
    --测试任务 第一个任务
    --任务创建部分
    local 任务 = luajava.newInstance("com.eztv.mud.LuaUtil");
    local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--奖励
    奖励:setExp(1350);--经验
    奖励:addItem(1, 1);--id 为1的物品给与1个
    任务:taskCreate("kill1", nil, nil, "击杀两只怪物");
    任务:taskCreateAction(1, 2);--id为1的怪物杀两只
    任务:taskAddCondition(Enum.taskType.kill);
    任务:setReward(奖励);
    --任务接受 逻辑 查看玩家是否接了任务，没有则添加，看是否完成
    local 该任务 =luaUtil:检查任务状态(client, 任务:getTask());--我身上的任务查  看玩家是否接了任务，没有则添加，看是否完成
    if (Enum.taskState.finished == 该任务:getTaskState()) then
        --任务完成 发放奖励
        窗口:setDesc("少侠做的不错！这是给你的奖励");
        luaUtil:添加选项("小事一桩", Enum.messageType.action, "doTalk", "", "testTask", true);
        --发送奖励
        luaUtil:发送奖励(client, 奖励);
        该任务:setTaskState(Enum.taskState.cant);--设置为不可接 以后循环任务可以操作
    elseif (Enum.taskState.processing == 该任务:getTaskState()) then
        --任务正在完成 显示进度--展示任务
        窗口:setDesc(任务:getTask():getDesc() .. "\n" .. 该任务:showTaskDetail());
        luaUtil:添加选项("我这就去把他们击溃", Enum.messageType.action, "doTalk", "", "testTask", true);
    else
        --已经做完任务
        窗口:setDesc("少侠快去别的地方探索吧");
        luaUtil:添加选项("谢谢", Enum.messageType.action, "doTalk", "", "", true);
    end
    窗口:添加选项集合(luaUtil);
    窗口:setCol(2);
    client:sendMsg(游戏工具:msgBuildForBytes(Enum.messageType.action, "doTalk", 游戏工具:object2JsonStr(窗口), 消息:getRole()));
end
