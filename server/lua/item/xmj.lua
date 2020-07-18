local attribute = luajava.newInstance("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
local luaUtil;
attribute:setAck(10);--设置10点攻击
function item_use(client,item,msg)--装备物品函数
    luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")--工具
    item:setEquipType(Enum.equipType.weapon);
    item:setAttribute(attribute);
    local useLevel = 2;
    if(luaUtil:getLevel(client)<useLevel)then--等级不够
        --code
        return;
    end
    luaUtil:doEquip(client,item);--穿上

end

function item_look(client,item,msg)--装备物品函数
end