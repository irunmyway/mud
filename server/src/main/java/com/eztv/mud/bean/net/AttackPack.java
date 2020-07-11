package com.eztv.mud.bean.net;

import com.eztv.mud.bean.Attribute;
import com.eztv.mud.bean.Enum;

public class AttackPack {
    private String who;
    private String target;
    private String desc;
    private Attribute targetAttribute;
    private Enum.gameObjectType targetType;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Enum.gameObjectType getTargetType() {
        return targetType;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Attribute getTargetAttribute() {
        return targetAttribute;
    }

    public void setTargetAttribute(Attribute targetAttribute) {
        this.targetAttribute = targetAttribute;
    }

    public void setTargetType(Enum.gameObjectType targetType) {
        this.targetType = targetType;
    }
}
