//例子 默认调用案例
var 村长任务
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
    窗口.内容("从这里踏上新的征程<br>我可以带你去这些地方");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
}

