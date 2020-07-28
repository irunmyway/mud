package com.eztv.mud.cache;

import com.eztv.mud.bean.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-28 19:33
 * 功能: 组队系统
 **/
public class TeamCache {
    private HashMap<String, List<GameObject>> teams = new HashMap<>();
    //添加成员
    public synchronized void addMember(String team,GameObject gameObject){
        if(teams.get(team)==null)teams.put(team,new ArrayList<>());
        teams.get(team).add(gameObject);
    }
    //删除成员
    public synchronized void removeMember(String team,GameObject gameObject){
        if(teams.get(team)==null)teams.put(team,new ArrayList<>());
        teams.get(team).remove(gameObject);
    }
    //解散队伍
    public synchronized void dropTeam(String team){
        teams.remove(team);
    }
}
