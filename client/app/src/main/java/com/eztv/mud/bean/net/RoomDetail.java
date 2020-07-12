package com.eztv.mud.bean.net;

import com.eztv.mud.bean.Monster;
import com.eztv.mud.bean.Npc;
import com.eztv.mud.bean.SendGameObject;

import java.util.ArrayList;
import java.util.List;

public class RoomDetail {
    private String name;
    private String top;
    private String down;
    private String left;
    private String right;

    private  List<SendGameObject> gameObjects = new ArrayList<>();

    public List<SendGameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<SendGameObject> gameObjects) {
        this.gameObjects = gameObjects;
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
