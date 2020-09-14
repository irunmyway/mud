//谷中法师
var 村长任务;
function 初始化(属性){
    //初始属性
    属性.setHp(10);
    属性.setHp_max(10);
    属性.setMp(50);
    属性.setMp_max(50);
    属性.setExp(999);
    属性.setExp_max(999);
    属性.setAtk(10);
}

function 对话(client, 窗口, 消息, 目标){
    if (消息.getRole() !=null) {
        if (消息.getRole() != ''){
            eval(消息.getRole()+"(client, 窗口, 消息,目标)");
            return;
        }
    }
    脚本工具.添加选项("新手村","action", 消息.getCmd(), 消息.getMsg(), "到新手村");
    脚本工具.添加选项("帮派主寨","action", 消息.getCmd(), 消息.getMsg(), "帮派主寨");
    //如果任务村长 才添加对话选项
    if ("processing" == 脚本工具.取任务状态(client,"testtalk")) {
        脚本工具.添加选项("村长的问话","action", 消息.getCmd(), 消息.getMsg(), "村长的问话");
    }
    窗口.内容("从这里踏上新的征程<br>我可以带你去这些地方");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
}

//传送功能
function 到新手村(client, 窗口, 消息, gameObj){
    脚本工具.到房间(client,"0","1");//到地图0 房间1
}
function 帮派主寨(client, 窗口, 消息, gameObj){
    脚本工具.到帮派房间(client,脚本工具.取帮派地图(client),101);//到地图0 房间1
}

//对话任务
function 村长的问话(client, 窗口, 消息, gameObj){
    村长任务=脚本工具.检查任务状态(client, "testtalk");
    村长任务.置状态("finished");
    窗口.内容("告诉村长，我答应了。"+"\n" + 村长任务.取任务详情());
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
}
