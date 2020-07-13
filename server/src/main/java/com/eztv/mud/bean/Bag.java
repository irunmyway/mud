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

    public List<String> toReward(Bag reward){//变成奖励
        List<String > list = new ArrayList<>();
        this.money+=reward.getMoney();
        this.ybMoney+=reward.getYbMoney();
        this.jbMoney+=reward.getJbMoney();
        if(reward.getMoney()>0)
        list.add("铜币 +"+reward.getMoney());
        if(reward.getJbMoney()>0)
        list.add("金币 +"+reward.getJbMoney());
        if(reward.getYbMoney()>0)
        list.add("元宝 +"+reward.getYbMoney());

        for(Item item:reward.items){
            list.add("获得了 "+item.getName()+" +"+item.getNum());
            int pos = this.items.indexOf(item);
            if(pos>-1){//叠加
                item.setNum(this.items.get(pos).getNum()+item.getNum());
                this.items.set(pos,item);
            }else{//新增
                this.items.add(item);
            }
        }
        return list;
    }
}
