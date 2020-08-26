//帮派地牢
function 初始化(房间) {
    房间.setPK(true);
}

function 进入房间(client, 窗口, 消息) {
    return 1;
}

function 挂机奖励(client, 窗口, 消息) {
    return 0;
}

function 地图查看(client, 窗口, 房间) {
    var str = 房间.getName() + "<br>&emsp;";
    str = str + 房间.getDesc() + "<br><purple>当前地图挂机奖励:</><br>&emsp;";
    str = str + "一朵小红花";
    窗口.内容(str);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}
