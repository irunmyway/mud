local attribute = luajava.newInstance("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")

--创建帮派事件
function 初始化(client,窗口)
    print("创建了帮派。执行下事件")
end
