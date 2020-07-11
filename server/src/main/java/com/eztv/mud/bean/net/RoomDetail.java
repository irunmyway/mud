package com.eztv.mud.bean.net;

import com.eztv.mud.bean.GameObject;
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

    private List<GameObject> npcList = new ArrayList<>();
    private List<GameObject> monsterList = new ArrayList<>();
    List<GameObject> playerList = new ArrayList<>();
    private List<SendGameObject> gameObjects = new ArrayList<>();

    public void addGameObject(SendGameObject obj){
        gameObjects.add(obj);
    }

    public List<SendGameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<SendGameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public List<GameObject> getNpcList() {
        return npcList;
    }

    public void setNpcList(List<GameObject> npcList) {
        this.npcList = npcList;
    }

    public List<GameObject> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(List<GameObject> monsterList) {
        this.monsterList = monsterList;
    }

    public List<GameObject> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<GameObject> playerList) {
        this.playerList = playerList;
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
