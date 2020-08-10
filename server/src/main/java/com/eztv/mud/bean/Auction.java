package com.eztv.mud.bean;

import com.eztv.mud.constant.Enum;
import online.sanen.cdm.template.jpa.Id;
import online.sanen.cdm.template.jpa.Table;

import java.util.Date;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-08-07 19:24
 * 功能: 寄卖
 **/
@Table(name = "t_auction")
public class Auction  {
    @Id
    private int id;
    private String role;//玩家id
    private int item;//物品id
    private Enum.itemType itemType;
    private long price;
    private Enum.currency currency;//交易类型
    private int num;
    private int total;
    private Date createat;
    private int state=0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public Enum.itemType getItemType() {
        return itemType;
    }

    public void setItemType(Enum.itemType itemType) {
        this.itemType = itemType;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Enum.currency getCurrency() {
        return currency;
    }

    public void setCurrency(Enum.currency currency) {
        this.currency = currency;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createat) {
        this.createat = createat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false ;
        }
        if (obj instanceof Item){
            Item item = (Item) obj;
            if(item.getId() == getId()&&item.getType()==((Item) obj).getType()){
                return true ;
            }
        }
        return false ;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
