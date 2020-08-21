package com.eztv.mud.admin.controller;

import com.eztv.mud.admin.dao.impl.RoomImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/api/room/")
public class RoomController {

    @Autowired
    private RoomImpl room;

    @RequestMapping("getRoom")
    public String getRoom(String content,String map) {
        return room.getRoom(map);
    }

    @RequestMapping("createRoom")
    public String createRoom(String map) {
        room.createRoom(map);
        return "success";
    }

    @RequestMapping("delRoom")
    public String delRoom(int id) {
        room.delRoom(id);
        return "success";
    }

    @RequestMapping("createRoomByBase")
    public String createRoomByBase(int id, String map, String direction, String name, String desc, String script) {
        room.createRoomByBase(id,map, direction, name, desc, script);
        return "success";
    }

    @RequestMapping("saveRoom")
    public String saveRoom(int id, String desc, String name, String script) {
        room.saveRoom(id, name, desc, script);
        return "success";
    }

    @RequestMapping("getMapOption")
    public String getMapOption() {
        return room.getMapOption();
    }

}
