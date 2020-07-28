local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具

--金币商城
function 初始化(client,窗口1,消息)
local a =状态()
    luaUtil:返回元素消息(client,"action","doTalk",目标:getKey(),窗口);
end
