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

}
