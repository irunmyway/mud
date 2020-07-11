package com.eztv.mud.bean.net;

import com.ez.utils.BObject;
import com.eztv.mud.bean.GameObject;

/**
 * 作者: hhx QQ1025334900
 * 时间: 2020-07-04 13:09
 * 功能: 房间
 **/
public class Room extends GameObject {
    private int id=0;
    private String name = "";//地图名称也是中心
    private String describe="";//地图介绍
    private int roomType=0;//安全区或者等等。。。。
    private String left = "";
    private String top = "";
    private String right = "";
    private String down = "";

    public Room(String name,String describe, String left, String top, String right, String down) {
        this.name = name;
        if(BObject.isNotEmpty(describe))
            this.describe = describe;
        if(BObject.isNotEmpty(left))
        this.left = left;
        if(BObject.isNotEmpty(top))
        this.top = top;
        if(BObject.isNotEmpty(right))
        this.right = right;
        if(BObject.isNotEmpty(down))
        this.down = down;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }
}
