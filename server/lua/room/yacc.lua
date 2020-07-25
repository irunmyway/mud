local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
local luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")

function 进入房间(client,窗口,消息)
    窗口:setDesc("此地十分凶险,你现在还进不去");
    luaUtil:添加选项("好吧", Enum.messageType.action, "", 消息:getMsg(), nil)
    窗口:添加选项集合(luaUtil);
    luaUtil:发送消息(client,游戏工具:msgBuildForBytes(Enum.messageType.action, "doTalk", 游戏工具:object2JsonStr(窗口), nil));
    return 0;
end
