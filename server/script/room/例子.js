function 初始化(房间){
    房间.setPK(true);//允许pk 默认都是true
    房间.setFly(true);//允许传送
}
function 进入房间(client,窗口,消息){//允许进入 false 不允许进入
    return true;
}

function 挂机奖励(client,窗口,消息){//地图挂机奖励配置
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

function 地图查看(client, 窗口, 房间) {//查看地图事件 用户点击

}
