package com.eztv.mud.bean;

public class Enum {
    //登录注册界面
    public enum login{login,register} //登录界面判断是哪一个
    public enum sex{male,female}//判断是男生女生

    //玩家状态
    public enum playerState{die,Login,unLogin,offLine}

    //玩家类型
    public enum gameObjectType{player,monster,npc}

    //物品类型
    public enum itemType{装备,药水,道具}

    //移动
    public enum direct{left,right,top,down}

    //通信模块
    public enum messageType{normal,login,toast,action,chat,input,unHandPop,pop}

    //聊天类型
    public enum chat{公聊,系统,私聊,本地}

    //窗口类型
    public  enum winAction{open,close,closeAll};
}
