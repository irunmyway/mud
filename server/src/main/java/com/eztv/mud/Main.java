package com.eztv.mud;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Server.getInstance().init();//加载服务端套接字

        DataBase.getInstance().init();//加载数据库框架

        Word.getInstance().init();//初始化整个世界地图及详细内容


        boolean run = true;
        Scanner sc = new Scanner(System.in);
        String notice = "《指令提示》\n重新加载公告:gg\n";
        while(run){
            String cmd = sc.nextLine();
            switch (cmd){
                case "ts":
                    System.out.println(notice);
                    break;
                case "gg":
                    Word.getInstance().initGG();
                    break;
                default:System.out.println(notice);
            }

        }

    }

}
