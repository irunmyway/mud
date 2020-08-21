local attribute = luajava.newInstance("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")
local lua工具=luajava.newInstance("com.eztv.mud.LuaUtil");
--登录事件 登录触发  可以用来写【挂机奖励】 或者【发送公告】
function 初始化(client,窗口)
    --执行挂机奖励 将会自动到玩家所挂机地图领取奖励
    lua工具:挂机奖励(client);
    --窗口:内容("你登录了游戏");
    --lua工具:返回元素消息(client,"action","doTalk",nil,窗口);
end


