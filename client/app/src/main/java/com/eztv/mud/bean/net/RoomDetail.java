package com.eztv.mud.bean.net;

import com.eztv.mud.bean.Monster;
import com.eztv.mud.bean.Npc;

import java.util.ArrayList;
import java.util.List;

public class RoomDetail {
    private String name;
    private String top;
    private String down;
    private String left;
    private String right;

    private List<Npc> npcList = new ArrayList<>();
    private List<Monster> monsterList = new ArrayList<>();
    List<Player> playerList = new ArrayList<>();

    public List<Npc> getNpcList() {
        return npcList;
    }

    public void setNpcList(List<Npc> npcList) {
        this.npcList = npcList;
    }
    public List<Player> getPlayerList() {
        return playerList;
    }



    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
    public List<Monster> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(List<Monster> monsterList) {
        this.monsterList = monsterList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
