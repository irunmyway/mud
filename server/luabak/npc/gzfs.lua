local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")
local 游戏工具 = luajava.bindClass("com.eztv.mud.GameUtil")
local lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
local fun = _G[funName];
local 村长任务
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
    if (消息:getRole() ~= nil) then
        if (消息:getRole() ~= '') then
            fun = _G[消息:getRole()]
            fun(client, 窗口, 消息, 目标);
            return ;
        end
    end
    lua工具:添加选项("新手村","action", 消息:getCmd(), 消息:getMsg(), "到新手村");
    --如果任务村长 才添加对话选项
    if ("processing" == lua工具:取任务状态(client,"testtalk")) then
        lua工具:添加选项("村长的问话","action", 消息:getCmd(), 消息:getMsg(), "村长的问话");
    end
    窗口:内容("从这里踏上新的征程<br>我可以带你去这些地方...");
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client,"action","doTalk",key,窗口);
end

--传送功能
function 到新手村(client, 窗口, 消息, gameObj)
    lua工具:到房间(client,"0","1");--到地图0 房间1
end

--对话任务
function 村长的问话(client, 窗口, 消息, gameObj)
    村长任务=lua工具:检查任务状态(client, "testtalk");
    村长任务:置状态("finished");
    窗口:内容("告诉村长，我答应了。".."\n" .. 村长任务:取任务详情());
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client,"action","doTalk",key,窗口);
end
