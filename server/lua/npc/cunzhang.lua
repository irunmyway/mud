local attribute = luajava.newInstance("com.eztv.mud.bean.Attribute")
local gameUtil = luajava.bindClass("com.eztv.mud.GameUtil")
local Choice = luajava.newInstance("com.eztv.mud.bean.Choice")--创建选项
local Enum = luajava.newInstance("com.eztv.mud.constant.Enum")
local luaUtil;
local fun =_G[funName];
function init()--初始属性
    attribute:setHp(999);
    attribute:setHp_max(999);
    attribute:setMp(50);
    attribute:setMp_max(50);
    attribute:setExp(999);
    attribute:setExp_max(999);
    attribute:setAck(10);
return gameUtil:object2JsonStr(attribute);
end


function talk(client,win,msg,gameObj)--任务功能
    luaUtil = luajava.newInstance("com.eztv.mud.LuaUtil")--初始化攻击
    if(msg:getRole()~=nil )then
        if(msg:getRole()~='') then
            fun =_G[msg:getRole()]
            fun(client,win,msg,gameObj);
            return;
        end
    end
    luaUtil:getChoice():add(Choice:createChoice("你好!",Enum.messageType.action,"",msg:getMsg(),'',true));
    luaUtil:getChoice():add(Choice:createChoice("讨伐怪物",Enum.messageType.action,"doTalk",msg:getMsg(),"testTask",true));
    win:setDesc("你好啊年轻人");
    win:setChoice(luaUtil:getChoice());
    client:sendMsg(gameUtil:msgBuildForBytes(Enum.messageType.action,"doTalk",gameUtil:object2JsonStr(win),key));
    luaUtil=nil;
end

function testTask(client,win,msg,gameObj)--测试任务 第一个任务
    --任务创建部分
    local task = luajava.newInstance("com.eztv.mud.LuaUtil");
    task:taskCreate("kill1",nil,nil,"击杀两只怪物");
    task:taskCreateAction(1,2);
    task:taskAddCondition(Enum.taskType.kill);
    --任务创建部分结束

    --任务接受 逻辑 查看玩家是否接了任务，没有则添加，看是否完成
    local mTask = client:getPlayer():getTaskHandler():checkTask(client,task:getTask());--我身上的任务
    --print("进度"..mTask:getProcess());--当前进度

    --展示客户部分
    luaUtil:getChoice():add(Choice:createChoice("我这就去把他们击溃",Enum.messageType.action,"doTalk","","testTask",false));
    win:setDesc(task:getTask():getDesc());
    win:setChoice(luaUtil:getChoice());
    win:setCol(2);
    client:sendMsg(gameUtil:msgBuildForBytes(Enum.messageType.action,"doTalk",gameUtil:object2JsonStr(win),msg:getRole()));
end
