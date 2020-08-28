package com.eztv.mud;

import android.widget.PopupWindow;

import com.eztv.mud.bean.net.Player;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-07-23 15:13
     * 功能: 常量
     **/
    public final static String IP="192.168.0.106";
//    public final static String IP="47.94.192.143";
    public final static String Port="1314";
    public final static long reconnectDelay = 3000;//重连延迟
    public final static boolean 通信检查 = true;


    /**
     * 作者: hhx QQ1025334900
     * 时间: 2020-07-23 15:14
     * 功能: 全局变量
     **/
    public static Player player = new Player();//游戏中的主角
    public static List<PopupWindow> popupWindows = new ArrayList<>();//所有打开的窗口
}
