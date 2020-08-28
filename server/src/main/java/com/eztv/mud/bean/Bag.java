package com.eztv.mud.bean;

import com.eztv.mud.GameUtil;
import com.eztv.mud.Word;
import com.eztv.mud.cache.SkillCache;
import com.eztv.mud.script.LuaOpen;

import java.util.ArrayList;
import java.util.List;

public class Bag implements LuaOpen.LuaBag, LuaOpen.LuaJson {
    List<Item> items = new ArrayList<>();
    private long money;
    private long jbMoney;
    private long ybMoney;
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
    public synchronized long changeYbMoney(long num) {
        return ybMoney+=num;
    }
    public synchronized long changeMoney(long num) {
        return money+=num;
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

    public Bag 新建(){
        return new Bag();
    }

    public void giveItem(int id, int num) {
        synchronized (items){
//            if (num < 1) num = 1;
            if (Math.abs(num)>1000) num = 1;
            Item addItem = null;
            for (Item item : Word.getInstance().getItems()) {
                if (item.getId() == id) {
                    addItem = item;
                }
            }
            if (addItem == null) return;
            int pos = items.indexOf(addItem);
            if (pos > -1) {//背包中有了
                items.remove(addItem);
                addItem.setNum(addItem.getNum() + num);
            } else {
                addItem.setNum(num);
            }
            items.add(addItem);
        }
    }

    public Item getItem(int id){
        for (Item item:items){
            if (item.getId()==id)return  item;
        }
        return null;
    }

    public void giveSkill(int id, int num) {
        synchronized (items){
            if (num < 1) num = 1;
            Item addItem = null;
            for (Item item : SkillCache.getSkills()) {
                if (item.getId() == id) {
                    addItem = item;
                }
            }
            if (addItem == null) return;
            int pos = items.indexOf(addItem);
            if (pos > -1) {//背包中有了
                items.remove(addItem);
                addItem.setNum(addItem.getNum() + num);
            } else {
                addItem.setNum(num);
            }
            items.add(addItem);
        }
    }

    public void delItem(int id, int num) {
        synchronized (items){
            if (num < 1) num = 1;
            Item delItem = null;
            for (Item item : items) {
                if (item.getId() == id) {
                    if (item.getNum() > 1) {
                        if (item.getNum() - num < 1) {
                            delItem = item;
                        } else {
                            item.setNum(item.getNum() - num);
                        }
                    } else {
                        delItem = item;
                    }
                }
            }
            if (delItem != null)
                items.remove(delItem);
        }
    }

    public int delItemBundle(int id) {
        int num = 0;
        synchronized (items){
            Item delItem = null;
            for (Item item : items) {
                if (item.getId() == id) {
                    delItem = item;
                }
            }
            if (delItem != null) {
                num = delItem.getNum();
                items.remove(delItem);
            }
        }
        return num;
    }


    @Override
    public void 给物品(int id, int num) {
        giveItem(id, num);
    }

    @Override
    public void 给技能(int id, int num) {
        giveSkill(id, num);
    }

    @Override
    public void 删物品(int id, int num) {
        delItem(id, num);
    }

    @Override
    public void 删物品集(int id) {
        delItemBundle(id);
    }

    @Override
    public void 给经验(long exp) {
        setExp(exp);
    }

    @Override
    public void 给铜币(long money) {
        setMoney(money);
    }

    @Override
    public void 给金币(long jb) {
        setJbMoney(jb);
    }

    @Override
    public void 给元宝(long yb) {
        setYbMoney(yb);
    }

    @Override
    public String 到文本() {
//        List<String> list = new ArrayList<>();
//        if(getJbMoney()!=0)
        String ret = "";
        if(getJbMoney()!=0){

        }

        return null;
    }

    @Override
    public String 到Json() {
        return GameUtil.object2JsonStr(this);
    }
}
