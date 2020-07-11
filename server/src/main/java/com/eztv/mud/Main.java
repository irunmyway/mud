package com.eztv.mud;

public class Main {

    public static void main(String[] args) {
        Server.getInstance().init();//加载服务端套接字

        DataBase.getInstance().init();//加载数据库框架

        Word.getInstance().init();//初始化整个世界地图及详细内容

    }

}
