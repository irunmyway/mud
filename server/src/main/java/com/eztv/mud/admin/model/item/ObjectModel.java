package com.eztv.mud.admin.model.item;

import com.eztv.mud.bean.Npc;

public class ObjectModel extends Npc {
    private String mapName;
    private String data;
    private String refreshment;

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
