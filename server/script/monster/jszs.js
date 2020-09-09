function 初始化(属性) {
    //基础属性初始化
    var 血量 = 脚本工具.取随机数(35, 45);
    属性.setHp(血量);
    属性.setHp_max(血量);
    属性.setMp(50);
    属性.setMp_max(50);
    属性.setExp(999);
    属性.setExp_max(999);
    属性.setEva(2);
    属性.setAcc(2);
    属性.setAtk(10);
}

function 对话(client, 窗口, 消息, 目标) {
    if (目标.getDesc() != null) {
        窗口.setDesc(目标.getName() + "<br>" + 目标.getDesc());
    } else {
        窗口.setDesc(目标.getName() + "<br>");
    }
    脚本工具.添加执行选项("攻击", "attack", 消息.getMsg(), null, "close", "red")
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client, "action", "doTalk", 目标.getKey(), 窗口);
}

function 死亡事件(client,窗口) {
    var 奖励 = 背包.新建()
    奖励.setMoney(115);//铜币
    奖励.setJbMoney(125);//金币
    奖励.setYbMoney(135);//元宝
    奖励.setExp(1350);//经验
    奖励.给物品(2, 1);//id 为1的物品给与1个
    奖励.给技能(1, 1);//id 为1的物品给与1个
    脚本工具.发送奖励(client, 奖励);
}

function 战斗事件(client,怪物,窗口) {

}

function 复活事件(client, 怪物) {
    脚本工具.全局系统消息(怪物.getName()+" 在新手村 复活辣。。。大家快来击杀");
}



