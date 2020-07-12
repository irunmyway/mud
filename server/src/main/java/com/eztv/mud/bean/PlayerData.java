package com.eztv.mud.bean;

public class PlayerData {
    private String room;
    private Attribute attribute = new Attribute();//玩家的基础属性
    private Bag bag = new Bag();


    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
