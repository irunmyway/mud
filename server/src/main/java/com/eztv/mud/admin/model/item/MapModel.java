package com.eztv.mud.admin.model.item;

import com.eztv.mud.GameUtil;

public class MapModel  {
    private int id;
    private String label;
    //map
    private long x;
    private long y;
    //edges
    private String from;
    private String to;
    private String arrows;

    private String desc;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getArrows() {
        return arrows;
    }

    public void setArrows(String arrows) {
        this.arrows = arrows;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toJson(){
        return GameUtil.object2JsonStr(this);
    }

    public void setDesc() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
