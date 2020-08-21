local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
local lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
function 初始化(房间)
    房间:setPK(true);
    return 房间:到Json();
end
function 进入房间(client,窗口,消息)
    窗口:setDesc("此地十分凶险,你现在还进不去");
    lua工具:添加选项("好吧", "action", "", 消息:getMsg(), nil)
    窗口:添加选项集合(lua工具);
    lua工具:返回元素消息(client,"action","doTalk",key,窗口);
    return 0;
end

function 挂机奖励(client,窗口,消息)
    --返回一个reward
    return 0;
end
