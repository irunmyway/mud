package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.dao.impl.MapImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/api/map/")
public class MapController {
    @Autowired
    MapImpl map;
    @RequestMapping("getMapList")
    public String getMapList(String value,int page, int limit){
        if(value!=null)return map.getMapListByWhere(value, page, limit);
        return map.getMapList(page, limit);
    }
    @RequestMapping("createMap")
    public String createMap(){
        if(map.createMap()){
            return "success";
        }
        return null;
    }
    @RequestMapping("delMap")
    public String delMap(String id){
        if(map.delMap(id))return "success";
        return null;
    }
    @RequestMapping("saveMap")
    public String saveItem(String id, String name) {
        if(map.saveMap(id,name))return "";
        return null;
    }
}
