//帮派使者
function 初始化(属性){
    //初始属性
    属性.setHp(100);
    属性.setHp_max(100);
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
    窗口.内容("从这里踏上新的征程<br>我可以带你去这些地方");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
}

//传送功能
function 到新手村(client, 窗口, 消息, gameObj){
    脚本工具.到房间(client,"0","1");//到地图0 房间1
}

