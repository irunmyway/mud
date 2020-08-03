package com.eztv.mud.admin.model;

import com.eztv.mud.GameUtil;
import com.eztv.mud.admin.model.item.MapModel;

import java.util.List;

public class MapSend<T> {
    private List<MapModel> nodes;
    private List<MapModel> edges;

    public List<MapModel> getNodes() {
        return nodes;
    }

    public void setNodes(List<MapModel> nodes) {
        this.nodes = nodes;
    }

    public List<MapModel> getEdges() {
        return edges;
    }

    public void setEdges(List<MapModel> edges) {
        this.edges = edges;
    }

    public String toJson(){
        return GameUtil.object2JsonStr(this);
    }
}
