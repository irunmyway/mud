package com.eztv.mud.bean;

import com.eztv.mud.Word;
import com.eztv.mud.utils.BDebug;

import java.util.ArrayList;
import java.util.List;

public class Bag {
    private long money;
    private long jbMoney;
    private long ybMoney;

    List<Item> items = new ArrayList<>();

    //奖励经验
    private long exp;

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getJbMoney() {
        return jbMoney;
    }

    public void setJbMoney(long jbMoney) {
        this.jbMoney = jbMoney;
    }

    public long getYbMoney() {
        return ybMoney;
    }

    public void setYbMoney(long ybMoney) {
        this.ybMoney = ybMoney;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void addItem(int id,int num){
        if(num<1)num=1;
        for(Item item:Word.getInstance().getItems()){
            if(item.getId()==id){
                item.setNum(num);
                items.add(item);
            }
        }
    }


}
