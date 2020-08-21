package com.eztv.mud.admin.dao;


public interface RoomDAO {
    String getRoom(String map);
    boolean saveRoom(int id,String name,String desc,String script);
    boolean createRoomByBase(int id,String map,String direction,String name,String desc,String script);
    boolean createRoom(String map);
    boolean delRoom(int id);
}
