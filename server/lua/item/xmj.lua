local attribute = luajava.newInstance("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
local luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")--工具
attribute:setAtk(10);--设置10点攻击
function item_use(client,item,win,msg)--装备物品函数
    item:setEquipType(Enum.equipType.weapon);
    item:setAttribute(attribute);
    local useLevel = 2;
    if(luaUtil:getLevel(client)<useLevel)then--等级不够
        win:setDesc("少侠你等级不够2级,无法驾驭。");
        luaUtil:getChoice():add(Choice:createChoice("原来如此",Enum.messageType.action,"doTalk","","testTask",true));
        win:setChoice(luaUtil:getChoice());
        win:setCol(2);
        client:sendMsg(gameUtil:msgBuildForBytes(Enum.messageType.action,"doTalk",gameUtil:object2JsonStr(win),key));
        return;
    end

    local detail = luaUtil:doEquip(client,item);--穿上后显示对比数据
    win:setDesc(detail);
    win:setCol(1);
    luaUtil:getChoice():add(Choice:createChoice("感觉强大了许多...",Enum.messageType.action,"doTalk","","testTask",true));
    win:setChoice(luaUtil:getChoice());
    client:sendMsg(gameUtil:msgBuildForBytes(Enum.messageType.action,"doTalk",gameUtil:object2JsonStr(win),key));
end

function item_look(client,item,win,msg)--装备物品函数
    item:setEquipType(Enum.equipType.weapon);
    item:setAttribute(attribute);

    luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")--工具
    win:setDesc(item:toDesc(Enum.itemType.equip));
    luaUtil:getChoice():add(Choice:createChoice("原来如此",Enum.messageType.action,"doTalk","","testTask",true));
    if(msg:getRole()~=nil)then
        luaUtil:getChoice():add(Choice:createChoice("卸下",Enum.messageType.action,"item_unload",item:getId(),nil,true));
    end
    win:setChoice(luaUtil:getChoice());
    win:setCol(2);
    client:sendMsg(gameUtil:msgBuildForBytes(Enum.messageType.action,"doTalk",gameUtil:object2JsonStr(win),key));
end