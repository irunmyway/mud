//万丈悬崖
function 初始化(房间){
    房间.setPK(true);
    房间.setFly(false);
}
function 进入房间(client,窗口,消息){
    窗口.内容("悬崖之下有什么秘密呢,只有在你有能力了才能知晓,你现在还不能进去.");
    脚本工具.添加选项("好吧", "action", "", 消息.getMsg(), null)
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
    return false;
}

function 挂机奖励(client,窗口,消息){
    return 0;
}

function 地图查看(client, 窗口, 房间) {

}
