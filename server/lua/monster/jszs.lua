local attribute = luajava.newInstance("com.eztv.mud.bean.Attribute")--属性结构体
local gameUtil = luajava.newInstance("com.eztv.mud.GameUtil")--游戏工具类
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")--创建选项
local bag = luajava.newInstance("com.eztv.mud.bean.Bag")--背包物品工具
local beanUtil = luajava.newInstance("com.eztv.mud.bean.BeanUtil")
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
function init()--基础属性初始化
    attribute:setHp(40);
    attribute:setHp_max(40);
    attribute:setMp(50);
    attribute:setMp_max(50);
    attribute:setExp(999);
    attribute:setExp_max(999);
    attribute:setAck(10);
return gameUtil:object2JsonStr(attribute);
end

function reward()--奖励
bag:setMoney(115);--铜币
bag:setJbMoney(125);--金币
bag:setYbMoney(135);--元宝
bag:setExp(1350);--经验
bag:addItem(1,1);--id 为1的物品给与1个
return gameUtil:object2JsonStr(bag);
end

function talk(client,win,msg,gameObj)--任务功能
--攻击模块
if(gameObj:getDesc()~=nil)then
    win:setDesc(gameObj:getName().."</p><br>&emsp;"..gameObj:getDesc());
else
    win:setDesc(gameObj:getName().."</p><br>&emsp;");
end
beanUtil:getChoice():add(Choice:createChoice("攻击",Enum.messageType.normal,"attack",""));
win:setChoice(beanUtil:getChoice());
client:sendMsg(gameUtil:msgBuildForBytes(Enum.messageType.action,"doTalk",gameUtil:object2JsonStr(win),gameObj:getKey()));
end

