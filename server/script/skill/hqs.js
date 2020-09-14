function 初始化(技能) {
    技能.hit(new Array(10,20,30,40));//对应等级对应的攻击
    技能.mp(new Array(10,20,30,40));//对应等级的攻击所扣除的 mp
    技能.pot(new Array(10,20,30,40));//对应等级升级所需要的点数
}
function 物品使用(client, 物品, 窗口, msg){
    物品.类型("skill");
    物品.属性(属性);
    var 使用等级 = 2;
    if (脚本工具.取等级(client) < 使用等级){
        窗口.内容("少侠你等级不够2级,无法学习。");
        脚本工具.添加选项("原来如此", "action", "doTalk", "", "testTask", "close");
        窗口.添加选项集合(脚本工具);
        窗口.列数(2);
        脚本工具.返回元素消息(client, "action", "doTalk", null, 窗口);
        return ;
    }
    var flag = 脚本工具.学习技能(client, 物品);
    if (flag) {
        脚本工具.返回系统消息(client, "你学习了技能 火球术");
    }else{
        脚本工具.返回系统消息(client, "你已经会了");
    }
}