//大赢家
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
    if (消息.getRole() !=null) {
        if (消息.getRole() != ''){
            eval(消息.getRole()+"(client, 窗口, 消息,目标)");
            return;
        }
    }
    脚本工具.添加选项("大赢家","action", 消息.getCmd(), 消息.getMsg(), "大赢家");
    窗口.内容("这里正在举行许多活动哦...快来参加吧!");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
}

//大赢家
function 大赢家(client, 窗口, 消息, gameObj){
    var money = 脚本工具.取全局变量("dyj");
    if(money==null)money=0;
    var str = "提示：大赢家目前累积铜币为<yellow>"+money+"</>，当达到<red>100</>以后，再投入金钱的玩家有机会获得全部铜币!"+
        "<br>投入金钱:";
    窗口.内容(str);
    脚本工具.添加选项("20铜币","action", 消息.getCmd(), 消息.getMsg(), "投20","closeAll");
    脚本工具.添加选项("30铜币","action", 消息.getCmd(), 消息.getMsg(), "投30","closeAll");
    窗口.添加选项集合(脚本工具);
    脚本工具.返回元素消息(client,"action","doTalk",null,窗口);
}
function 投20(client, 窗口, 消息, gameObj){
    var 奖励 = 背包.新建();
    奖励.给铜币(-20);
    脚本工具.发送奖励(client, 奖励);
    增删大赢家金钱(client,20);
}
function 投30(client, 窗口, 消息, gameObj){
    var 奖励 = 背包.新建();
    奖励.给铜币(-30);
    脚本工具.发送奖励(client, 奖励);
    增删大赢家金钱(client,30);
}
function 增删大赢家金钱(client,value) {
    var money = 脚本工具.取全局变量("dyj");
    if(money==null){
        if(value<0)value=0;
        脚本工具.置全局变量("dyj",Number(value));
    }else{
        var newMoney = Number(money)+Number(value);
        if(newMoney>100){//可以开盘
            var 随机数 = 脚本工具.取随机数(1,100);
            if(随机数<30){
                var 玩家 = client.getPlayer().getName();
                var 奖励 = 背包.新建();
                奖励.给铜币(newMoney);
                脚本工具.发送奖励(client, 奖励);
                脚本工具.置全局变量("dyj",0);
                脚本工具.全局系统消息("本次大赢家为<yellow>"+玩家+"</>，他1个人独享铜币<red>"+newMoney+"</>");
                return;
            }
        }
        脚本工具.置全局变量("dyj",newMoney);
    }
}
