//豪华新手礼包
function 物品使用(client, 物品, 窗口, msg) {


}

function 物品查看(client, 物品, 窗口, msg) {
    窗口.内容(物品.getName() + "新人豪华大礼包");
    窗口.添加选项集合(脚本工具);
    窗口.列数(2);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}

