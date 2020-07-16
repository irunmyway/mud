local attribute = luajava.bindClass("com.eztv.mud.bean.Attribute")--属性结构体
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")--游戏工具类
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")--创建选项
local bag = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
local valset = luajava.newInstance("com.eztv.mud.test.Test")--背包物品工具
local jszs = luajava.new(attribute)
local util = luajava.newInstance("com.eztv.mud.bean.BeanUtil")
function init()--基础属性初始化
jszs:setHp(40);
jszs:setHp_max(40);
jszs:setMp(50);
jszs:setMp_max(50);
jszs:setExp(999);
jszs:setExp_max(999);
jszs:setAck(10);
return gameUtil:object2JsonStr(jszs);
end

function reward()--奖励
bag:setMoney(115);--铜币
bag:setJbMoney(125);--金币
bag:setYbMoney(135);--元宝
bag:setExp(1350);--经验
bag:addItem(1,1);--id 为1的物品给与1个
return gameUtil:object2JsonStr(bag);
end

function choice()--基础功能
local util = luajava.newInstance("com.eztv.mud.bean.BeanUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
util:getChoice():add(Choice:createChoice("攻击",Enum.messageType.normal,"attack",""));
return gameUtil:objectArr2JsonStr(util:getChoice());
end

function getVal(obj)
    print(obj:getA());
    return obj:getA();
end

