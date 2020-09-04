function 初始化(房间) {
    房间.setPK(false);
}

function 进入房间(client, 窗口, 消息) {
    return true;
}

function 挂机奖励(client, 窗口, 消息) {
    var 分钟数 = 脚本工具.取离线时间(client);
    if (分钟数 > 0) {
        背包.给经验(50 * 分钟数 * 脚本工具.取随机数(1, 8));//经验
        背包.给物品(1, 脚本工具.取随机数(1, 8));//id 为1的物品给与1个
        背包.给技能(1, 脚本工具.取随机数(1, 8));//id 为1的物品给与1个
        脚本工具.发送奖励(client, 背包);
        窗口.内容("离线了：" + 分钟数 + "分钟");
        脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
    }
    return 0;
}

function 地图查看(client, 窗口, 房间) {
    var str = 房间.getName() + "<br>&emsp;";
    str = str + 房间.getDesc() + "<br><purple>当前地图挂机奖励:</><br>&emsp;";
    str = str + "一朵小红花";
    窗口.内容(str);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}
