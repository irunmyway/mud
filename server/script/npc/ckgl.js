//仓库管理
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
    消息数据 = 脚本工具.到消息(消息.getRole());
    if(消息数据.取值("cmd")!=null) {
        eval(消息数据.取值("cmd")+"(client, 窗口, 消息)");
        return;
    }
    窗口.内容("寄卖系统<br>&emsp;<green>寄卖物品收取一定的手续费</>");
    脚本工具.添加执行选项("寄卖商城", 消息.getCmd(), 消息.getMsg(), 消息数据.添加("cmd","寄卖商城").到文本(), "open", "red");
    脚本工具.添加执行选项("寄卖物品", "jmSell", null, 消息数据.添加("cmd","寄卖物品").到文本(), "open", "blue");
    脚本工具.添加执行选项("寄卖货币", "jmSell", 消息数据.添加("cmd","寄卖货币").到文本(), null, "close", "blue");
    脚本工具.添加执行选项("获取收益", "jmReward", null, null, "close", "yellow");
    脚本工具.添加执行选项("我的寄卖", "jmLook", 消息数据.添加("cmd","我的寄卖").到文本(), null, "open", "blue");
    脚本工具.添加执行选项("寄卖说明", 消息.getCmd(), 消息.getMsg(), 消息数据.添加("cmd","寄卖说明").到文本(), "open", "red");

    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}

function 寄卖商城 (client, 窗口, 消息){
    窗口.内容("选择商城类型");
    窗口.列数(2)
    脚本工具.添加执行选项("所有", "jmLook", 消息数据.添加("cmd","所有").到文本(), null, "open", "blue");
    脚本工具.添加执行选项("商品", "jmLook", 消息数据.添加("cmd","商品").到文本(), null, "open", "blue");
    脚本工具.添加执行选项("技能", "jmLook", 消息数据.添加("cmd","技能").到文本(), null, "open", "blue");
    脚本工具.添加执行选项("货币", "jmLook", 消息数据.添加("cmd","货币").到文本(), null, "open", "blue");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}

function 寄卖说明 (client, 窗口, 消息){
    窗口.内容("寄卖的物品在【<green>一定时间内</>】没人购买将会退还<br>寄卖后的物品和退还的物品在【<red>获取收益中</>】取回");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}