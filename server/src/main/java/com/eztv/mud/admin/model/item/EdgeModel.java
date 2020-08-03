package com.eztv.mud.admin.model.item;

import com.eztv.mud.GameUtil;

public class EdgeModel {
    private String label;
    //edges
    private String from;
    private String to;
    private String arrows;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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


    public String toJson(){
        return GameUtil.object2JsonStr(this);
    }
}
