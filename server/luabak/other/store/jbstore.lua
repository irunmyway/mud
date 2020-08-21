local 属性 = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local 游戏工具 = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local 奖励 = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
lua工具 = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
local 商城={--格式为 名称 类型 id 价格
    {"药水","normal","1",100},
    {"小木剑/1金币","normal","2",130},
}
--金币商城
function 初始化(client,窗口,消息)
    if(消息:getRole()~=nil)then
        if(消息:getMsg()==nil)then
            执行购买(client,窗口,消息);
        else
            初始化购买(client,窗口,消息);
        end
    else
        index=-1
        for i, v in ipairs(商城) do
            lua工具:添加选项(v[1], "action", "jbStore", v[2], v[1], "close");
        end
        窗口:添加选项集合(lua工具)
        lua工具:返回元素消息(client,"action","doTalk",nil,窗口);
    end
end

function 初始化购买(client,窗口,消息)
    if(存在商城(消息:getRole(),商城))then
        临时物品 = {消息:getRole(),消息:getMsg()}--名称  类型
    else
        临时物品 = { 0,""}
    end
    窗口:内容("选择购买的数量哦。");
    lua工具:添加执行选项("买1个", "jbStore", nil, "1", "closeAll","red");
    lua工具:添加执行选项("买10个","jbStore", nil, "10", "closeAll","yellow");
    lua工具:添加执行选项("买50个","jbStore", nil, "10", "closeAll","blue");
    窗口:添加选项集合(lua工具)
    lua工具:返回元素消息(client,"action","doTalk",nil,窗口);
end

function 执行购买(client,窗口,消息)
    if(存在商城(临时物品[1],商城))then
        if (消息:getRole()~=nie) then
            if(临时物品[2]=="normal")then
                local 要买的物品 = 获取商品(临时物品[1]);
                lua工具:购买(client,要买的物品[3],消息:getRole(),要买的物品[4]);
            else
                lua工具:购买技能(client,要买的物品[3],消息:getRole(),要买的物品[4]);
            end

        end
    end
end
function 获取商品(value)
    for k,v in ipairs(商城) do
        if v[1] == value then
            return v;
        end
    end
    return 0;
end
function 存在商城(value, tbl)
    for k,v in ipairs(tbl) do
        if v[1] == value then
            return true;
        end
    end
    return false;
end

