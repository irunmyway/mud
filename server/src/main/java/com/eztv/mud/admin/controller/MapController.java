package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.dao.impl.MapImpl;
import com.eztv.mud.utils.BDebug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map/")
public class MapController {

    @Autowired
    private MapImpl map;

    @RequestMapping("getMap")
    public String saveGG(String content) {
        return map.getMap();
    }

    @RequestMapping("createRoom")
    public String createRoom() {
        BDebug.trace("测试1");
        map.createRoom();
        BDebug.trace("测试2");
        return "success";
    }

    @RequestMapping("delRoom")
    public String delRoom(int id) {
        BDebug.trace("测试1");
        map.delRoom(id);
        BDebug.trace("测试2");
        return "success";
    }

    @RequestMapping("createRoomByBase")
    public String createRoomByBase(int id, String direction, String name, String desc, String script) {
        map.createRoomByBase(id, direction, name, desc, script);
        return "success";
    }

    @RequestMapping("saveRoom")
    public String saveRoom(int id, String desc, String name, String script) {
        map.saveRoom(id, name, desc, script);
        return "success";
    }

    @RequestMapping("getMapOption")
    public String getMapOption() {
        return map.getMapOption();
    }

}
