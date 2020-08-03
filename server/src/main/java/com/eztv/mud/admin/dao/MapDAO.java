package com.eztv.mud.admin.dao;


public interface MapDAO {
    String getMap();
    boolean saveRoom(int id,String name,String desc,String script);
    boolean createRoomByBase(int id,String direction,String name,String desc,String script);
    boolean createRoom();
    boolean delRoom(int id);
}
