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

function 击杀奖励(client,窗口) {
    var 奖励 = 背包.新建()
    奖励.setMoney(115);//铜币
    奖励.setJbMoney(125);//金币
    奖励.setYbMoney(135);//元宝
    奖励.setExp(1350);//经验
    return 奖励;
}

function 对话(client, 窗口, 消息, 目标) {
    if (目标.getDesc() != null) {
        窗口.setDesc(目标.getName() + "<br>" + 目标.getDesc());
    } else {
        窗口.setDesc(目标.getName() + "<br>");
    }
    脚本工具.添加执行选项("杀死", "attack", 消息.getMsg(), null, "close", "red")
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client, "action", "doTalk", 目标.getKey(), 窗口);
}

function 复活事件(client, 怪物) {
}

