local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
local luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
属性:setAtk(10);--设置10点攻击
function 物品使用(client,物品,窗口,msg)
    物品:setEquipType(Enum.equipType.weapon);
    物品:setAttribute(属性);
    local 使用等级 = 2;
    if(luaUtil:getLevel(client)<使用等级)then--等级不够
        窗口:setDesc("少侠你等级不够2级,无法驾驭。");
        luaUtil:添加选项("原来如此",Enum.messageType.action,"doTalk","","testTask",true);
        窗口:添加选项集合(luaUtil);
        窗口:setCol(2);
        client:sendMsg(游戏工具:msgBuildForBytes(Enum.messageType.action,"doTalk",游戏工具:object2JsonStr(窗口),key));
        return;
    end
    local detail = luaUtil:doEquip(client,物品);--穿上后显示对比数据
    窗口:setDesc(detail);
    窗口:setCol(1);
    luaUtil:添加选项("感觉强大了许多...",Enum.messageType.action,"doTalk","","testTask",true);
    窗口:添加选项集合(luaUtil);
    client:sendMsg(游戏工具:msgBuildForBytes(Enum.messageType.action,"doTalk",游戏工具:object2JsonStr(窗口),key));
end

function 物品查看(client,物品,窗口,msg)
    物品:setEquipType(Enum.equipType.weapon);
    物品:setAttribute(属性);

    luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")--工具
    窗口:setDesc(物品:toDesc(Enum.itemType.equip));
    luaUtil:添加选项("原来如此",Enum.messageType.action,"doTalk","","testTask");
    if(msg:getRole()~=nil)then
        luaUtil:添加选项("卸下",Enum.messageType.action,"item_unload",物品:getId(),nil,Enum.winAction.closeAll);
    end
    窗口:添加选项集合(luaUtil);
    窗口:setCol(2);
    client:sendMsg(游戏工具:msgBuildForBytes(Enum.messageType.action,"doTalk",游戏工具:object2JsonStr(窗口),key));
end