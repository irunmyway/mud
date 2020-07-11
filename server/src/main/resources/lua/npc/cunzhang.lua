local attribute = luajava.bindClass("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")
local jszs = luajava.new(attribute)
function init()
jszs:setHp(999);
jszs:setHp_max(999);
jszs:setMp(50);
jszs:setMp_max(50);
jszs:setExp(999);
jszs:setExp_max(999);
jszs:setAck(10);
return gameUtil:object2JsonStr(jszs);
end




--无参函数
function hello()
print 'hello'
end
--带参函数
function test(str)
print('data from java is:'..str)
return 'haha'
end