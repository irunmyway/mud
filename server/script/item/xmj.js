属性.setAtk(10);//设置10点攻击
function 物品使用(client, 物品, 窗口, msg) {
    物品.类型("weapon");
    物品.属性(属性);
    var 使用等级 = 2;
    if (脚本工具.取等级(client) < 使用等级) {
        窗口.内容("少侠你等级不够2级,无法驾驭。");
        脚本工具.添加选项("原来如此", "action", "doTalk", "", "testTask", true);
        窗口.添加选项集合(脚本工具);
        窗口.列数(2);
        脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
        return;
    }
    var 对比详情 = 脚本工具.装备(client, 物品);//穿上后显示对比数据
    窗口.内容(对比详情);
    窗口.列数(1);
    脚本工具.添加选项("感觉强大了许多...", "action", "doTalk", "", "testTask");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}

function 物品查看(client, 物品, 窗口, msg) {
    脚本工具.添加选项("原来如此", "action", "doTalk", "", "testTask");
    if (msg.getRole() != null) {
        脚本工具.添加选项("卸下", "action", "item_unload", 物品.getId(), null, "closeAll");
    }
    物品.类型("weapon");
    物品.属性(属性);
    窗口.内容(物品.getName() + "</p><br>" + 物品.到文本("equip"));
    窗口.添加选项集合(脚本工具);
    窗口.列数(2);
    脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
}

