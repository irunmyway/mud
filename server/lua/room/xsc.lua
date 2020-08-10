local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--奖励
function 进入房间(client, 窗口, 消息)
    return 1;
end

function 挂机奖励(client, 窗口, 消息)
    local 分钟数 = lua工具:取离线时间(client);
    if 分钟数 > 0 then
        奖励:给经验(50 * 分钟数 * lua工具:取随机数(1, 8));--经验
        奖励:给物品(1, lua工具:取随机数(1, 8));--id 为1的物品给与1个
        奖励:给技能(1, lua工具:取随机数(1, 8));--id 为1的物品给与1个
        lua工具:发送奖励(client, 奖励);
        窗口:setDesc("离线了：" .. 分钟数 .. "分钟");
        lua工具:返回元素消息(client, "action", "doTalk", nil, 窗口);
    end
    return 0;
end
