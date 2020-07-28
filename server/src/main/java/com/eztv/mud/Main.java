package com.eztv.mud;

import com.eztv.mud.cache.ItemCache;
import com.eztv.mud.cache.SkillCache;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Server.getInstance().init();//加载服务端套接字0
        DataBase.getInstance().init();//加载数据库框架
        Word.getInstance().init();//初始化整个世界地图及详细内容
        boolean run = true;
        Scanner sc = new Scanner(System.in);
        String notice = "《指令提示》\n" +
                "重新加载公告:gg\n" +
                "通信检查:cknet\n" +
                "退出:tc\n"+
                "更新技能:skill\n"+
                "更新物品:item\n"+
                "更新地图: npc 怪物 :env\n";
        while(run){
            String cmd = sc.nextLine();
            switch (cmd){
                case "gg":
                    Word.getInstance().initGG();
                    break;
                case "cknet":
                    System.out.println((Constant.set通信检查()?"打开了通信检测":"关闭了通信检查"));
                    break;
                case "item":
                    ItemCache.initItem(Word.getInstance().getGlobals());
                    break;
                case "skill":
                    SkillCache.initSkill(Word.getInstance().getGlobals());
                    break;
                case "env":
                    Word.getInstance().initEnvironment();
                    break;
                default:System.out.println(notice);
            }

        }

    }
}
