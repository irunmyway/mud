local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")
local 游戏工具 = luajava.bindClass("com.eztv.mud.GameUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
local lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
local fun = _G[funName];
function 初始化()
    --初始属性
    属性:setHp(10);
    属性:setHp_max(10);
    属性:setMp(50);
    属性:setMp_max(50);
    属性:setExp(999);
    属性:setExp_max(999);
    属性:setAtk(10);
    return 属性:到Json();
end

function 对话(client, 窗口, 消息, 目标)
    消息数据 = lua工具:到消息(消息:getRole());
    if 消息数据:取值("cmd")~=nie then
        local fun = _G[消息数据:取值("cmd")]
        fun(client, 窗口, 消息);
        return;
    end
    窗口:内容("寄卖系统<br>&emsp;<green>寄卖物品收取一定的手续费</>");
    lua工具:添加执行选项("寄卖商城", 消息:getCmd(), 消息:getMsg(), 消息数据:添加("cmd","寄卖商城"):到文本(), "open", "red");
    lua工具:添加执行选项("寄卖物品", "jmSell", nil, 消息数据:添加("cmd","寄卖物品"):到文本(), "open", "blue");
    lua工具:添加执行选项("寄卖货币", "jmSell", 消息数据:添加("cmd","寄卖货币"):到文本(), nil, "close", "blue");
    lua工具:添加执行选项("获取收益", "jmReward", nil, nil, "close", "yellow");
    lua工具:添加执行选项("我的寄卖", "jmLook", 消息数据:添加("cmd","我的寄卖"):到文本(), nil, "open", "blue");
    lua工具:添加执行选项("寄卖说明", 消息:getCmd(), 消息:getMsg(), 消息数据:添加("cmd","寄卖说明"):到文本(), "open", "red");

    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client, "action", "doTalk", nil, 窗口);
end

function 寄卖商城 (client, 窗口, 消息)
    窗口:内容("选择商城类型");
    窗口:列数(2)
    lua工具:添加执行选项("所有", "jmLook", 消息数据:添加("cmd","所有"):到文本(), nil, "open", "blue");
    lua工具:添加执行选项("商品", "jmLook", 消息数据:添加("cmd","商品"):到文本(), nil, "open", "blue");
    lua工具:添加执行选项("技能", "jmLook", 消息数据:添加("cmd","技能"):到文本(), nil, "open", "blue");
    lua工具:添加执行选项("货币", "jmLook", 消息数据:添加("cmd","货币"):到文本(), nil, "open", "blue");
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client, "action", "doTalk", nil, 窗口);
end

function 寄卖说明 (client, 窗口, 消息)
    窗口:内容("寄卖的物品在【<green>一定时间内</>】没人购买将会退还<br>寄卖后的物品和退还的物品在【<red>获取收益中</>】取回");
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client, "action", "doTalk", nil, 窗口);
end