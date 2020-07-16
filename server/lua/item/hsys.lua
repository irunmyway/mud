local attribute = luajava.bindClass("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")
local jszs = luajava.new(attribute)
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
local util = luajava.newInstance("com.eztv.mud.bean.BeanUtil")
local bag = luajava.newInstance("com.eztv.mud.bean.Bag")
bag:setMoney(115);--铜币
bag:setJbMoney(125);--金币
bag:setYbMoney(135);--元宝
return gameUtil:object2JsonStr(bag);
end

function choice()--基础功能
local util = luajava.newInstance("com.eztv.mud.bean.BeanUtil")
local Enum = luajava.newInstance("com.eztv.mud.bean.Enum")
util:getChoice():add(Choice:createChoice("攻击",Enum.messageType.normal,"attack",""));
return gameUtil:objectArr2JsonStr(util:getChoice());
end

