local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
local lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
属性:setAtk(10);--设置10点攻击
function 物品使用(client, 物品, 窗口, msg)
    物品:类型("weapon");
    物品:属性(属性);
    local 使用等级 = 2;
    if (lua工具:当前等级(client) < 使用等级) then
        --等级不够
        窗口:内容("少侠你等级不够2级,无法驾驭。");
        lua工具:添加选项("原来如此", "action", "doTalk", "", "testTask", true);
        窗口:添加选项集合(lua工具);
        窗口:列数(2);
        lua工具:返回元素消息(client, "action", "doTalk", key, 窗口);
        return ;
    end
    local 对比详情 = luaUtil:装备(client, 物品);--穿上后显示对比数据
    窗口:内容(对比详情);
    窗口:列数(1);
    lua工具:添加选项("感觉强大了许多...", "action", "doTalk", "", "testTask", true);
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client, "action", "doTalk", key, 窗口);
end

function 物品查看(client, 物品, 窗口, msg)
    lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")--工具
    lua工具:添加选项("原来如此", "action", "doTalk", "", "testTask");
    if (msg:getRole() ~= nil) then
        lua工具:添加选项("卸下", "action", "item_unload", 物品:getId(), nil, "closeAll");
    end
    物品:类型("weapon");
    物品:属性(属性);
    窗口:内容(物品:getName() .. "</p><br>" .. 物品:到文本("equip"));
    窗口:添加选项集合(lua工具);
    窗口:列数(2);
    lua工具:返回元素消息(client, "action", "doTalk", key, 窗口);
end