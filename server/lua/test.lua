local attribute = luajava.bindClass("com.eztv.mud.bean.Attribute")--属性结构体
lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
function getVal()
    print(lua工具:取随机数(1,5))
end

