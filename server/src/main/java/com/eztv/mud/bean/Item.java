package com.eztv.mud.bean;

public class Item extends GameObject{
    private String name;
    private int num;


    public Item(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public Attribute getAttribute() {
        return null;
    }
}
