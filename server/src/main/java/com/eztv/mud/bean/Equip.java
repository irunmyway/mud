package com.eztv.mud.bean;

public class Equip {
    private Item weapon= new Item();//武器
    private Item head=new Item();//头冠
    private Item cloth=new Item();;//衣服
    private Item pants=new Item();;//裤子
    private Item shoes=new Item();;//鞋子


    public Item getWeapon() {
        return weapon;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public Item getHead() {
        return head;
    }

    public void setHead(Item head) {
        this.head = head;
    }

    public Item getCloth() {
        return cloth;
    }

    public void setCloth(Item cloth) {
        this.cloth = cloth;
    }

    public Item getPants() {
        return pants;
    }

    public void setPants(Item pants) {
        this.pants = pants;
    }

    public Item getShoes() {
        return shoes;
    }

    public void setShoes(Item shoes) {
        this.shoes = shoes;
    }

    public Attribute calculate(){//装备的总属性
       return weapon.getAttribute().add(head.getAttribute()).add(cloth.getAttribute()).add(pants.getAttribute()).add(shoes.getAttribute());
    }

}

