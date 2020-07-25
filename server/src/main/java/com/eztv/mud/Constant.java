package com.eztv.mud;

import com.eztv.mud.bean.Client;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static List<Client> clients = new ArrayList();

    public final static String DEFAULT_ROOM_ID = "1";//默认初始房间
    public final static int FIGHT_SPEED = 1200;//杀怪速度

    public final static String NPC_PATH = "lua/npc/";//NPC目录
    public final static String Monster_PATH = "lua/monster/";//NPC目录
    public final static String Item_PATH = "lua/item/";//物品目录
    public final static String Room_PATH = "lua/room/";//房间目录
    public final static String Other_PATH = "lua/other/";//房间目录


    public static boolean 通信检查 = true;
    public  static long 通信延迟 = 0;


    //游戏展示
    public final static String STR_TITLE = "</p><br>&emsp;";
    public static boolean set通信检查() {
        return Constant.通信检查 = !Constant.通信检查;
    }



    //数据库语句
    public final static String FactionListSql = "select * from t_faction";



    //lua 指令
    public final static String LUA_对话="对话";
    public final static String LUA_击杀奖励="击杀奖励";
    public final static String LUA_初始化="初始化";
    public final static String LUA_进入房间="进入房间";

}
