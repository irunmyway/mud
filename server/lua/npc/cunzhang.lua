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
    local bag = luajava.newInstance("com.eztv.mud.bean.Bag")--奖励
    bag:setExp(1350);--经验
    bag:addItem(1,1);--id 为1的物品给与1个
    task:taskCreate("kill1",nil,nil,"击杀两只怪物");
    task:taskCreateAction(1,2);
    task:taskAddCondition(Enum.taskType.kill);
    task:setReward(bag);
    --任务接受 逻辑 查看玩家是否接了任务，没有则添加，看是否完成
    local mTask = client:getPlayer():getTaskHandler():checkTask(client,task:getTask());--我身上的任务查  看玩家是否接了任务，没有则添加，看是否完成

    if(Enum.taskState.finished==mTask:getTaskState())then--任务完成 发放奖励
        win:setDesc("少侠做的不错！这是给你的奖励");
        luaUtil:getChoice():add(Choice:createChoice("小事一桩",Enum.messageType.action,"doTalk","","testTask",true));
        --发送奖励
        luaUtil:senReward(client,bag);
        mTask:setTaskState(Enum.taskState.cant);--设置为不可接 以后循环任务可以操作
    elseif(Enum.taskState.processing==mTask:getTaskState())then--任务正在完成 显示进度--展示任务
        win:setDesc(task:getTask():getDesc().."\n"..mTask:showTaskDetail());
        luaUtil:getChoice():add(Choice:createChoice("我这就去把他们击溃",Enum.messageType.action,"doTalk","","testTask",true));
    else--已经做完任务
        win:setDesc("少侠快去别的地方探索吧");
        luaUtil:getChoice():add(Choice:createChoice("谢谢",Enum.messageType.action,"doTalk","","",true));
    end
    win:setChoice(luaUtil:getChoice());
    win:setCol(2);
    client:sendMsg(gameUtil:msgBuildForBytes(Enum.messageType.action,"doTalk",gameUtil:object2JsonStr(win),msg:getRole()));
end
