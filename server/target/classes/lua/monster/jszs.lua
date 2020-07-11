local attribute = luajava.bindClass("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")

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

function choice()--基础功能
local util = luajava.newInstance("com.eztv.mud.bean.BeanUtil")
local Enum = luajava.newInstance("com.eztv.mud.bean.Enum")
util:getChoice():add(createChoice("攻击",Enum.messageType.normal,"attack",""));
return gameUtil:objectArr2JsonStr(util:getChoice());
end

function createChoice(name,type,cmd,msg)--创建选项
local choiceRet = luajava.newInstance("com.eztv.mud.bean.Choice")
choiceRet:setName(name);
choiceRet:setCmd(cmd);
choiceRet:setMsg(msg);
choiceRet:setType(type);
return choiceRet;
end
