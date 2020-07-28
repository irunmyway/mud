local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
function 初始化()
    --基础属性初始化
    属性:setHp(40);
    属性:setHp_max(40);
    属性:setMp(50);
    属性:setMp_max(50);
    属性:setExp(999);
    属性:setExp_max(999);
    属性:setEva(2);
    属性:setAcc(2);
    属性:setAtk(10);
    return 游戏工具:object2JsonStr(属性);
end

function 击杀奖励()
    奖励:setMoney(115);--铜币
    奖励:setJbMoney(125);--金币
    奖励:setYbMoney(135);--元宝
    奖励:setExp(1350);--经验
    奖励:给物品(2, 1);--id 为1的物品给与1个
    奖励:给技能(1, 1);--id 为1的物品给与1个
    return 游戏工具:object2JsonStr(奖励);
end


function 对话(client, 窗口, 消息, 目标)
    luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
    if (目标:getDesc() ~= nil) then
        窗口:setDesc(目标:getName() .. "<br>" .. 目标:getDesc());
    else
        窗口:setDesc(目标:getName() .. "<br>");
    end
    luaUtil:添加选项("攻击", Enum.messageType.normal, "attack", 消息:getMsg(), nil)
    窗口:添加选项集合(luaUtil);
    luaUtil:返回元素消息(client,"action","doTalk",目标:getKey(),窗口);
end

