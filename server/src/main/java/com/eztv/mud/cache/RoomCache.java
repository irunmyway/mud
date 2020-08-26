package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Room;
import com.eztv.mud.script.ScriptExecutor;
import com.eztv.mud.utils.BDebug;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.*;

public class RoomCache {
    public static HashMap<String, HashMap<String, Room>> maps = new HashMap<>();
    private static ScriptExecutor scriptExecutor;

    public static void initRooms() {//初始化所有房间
        scriptExecutor = new ScriptExecutor();
        //初始化所有房间
        maps.clear();
        List<Room> roomList = DataBase.getInstance().init().createSQL("select * from t_map_room").list(Room.class);
        int roomNum = 0;
        for (Room room : roomList) {
            try {
                addRoom(room);
                roomNum++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BDebug.trace("房间加载完成 数量 : Map load RoomNum:【" + roomNum + "】");
    }
    //添加地图
    public static void addMap(Collection<Room> rooms) {
        rooms.forEach(room -> {
            addRoom(room);
        });
    }
    //添加房间
    public static void addRoom(Room room) {
        if (room.getScript().length() > 0) {//模板
            room.setScript(Room_PATH + room.getScript());
            try{
                scriptExecutor.load(room.getScript()).execute(脚本_初始化, room);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        synchronized (maps) {
            if (maps.get(room.getMap() + "") == null)
                maps.put(room.getMap() + "", new HashMap<>());
            maps.get(room.getMap() + "").put(room.getId() + "", room);
        }
    }


    //通过地图获取【该地图】所有房间
    public static HashMap<String, Room> getRoomsByMap(Object id) {
        return maps.get(id );
    }

    //【默认世界地图】
    public static HashMap<String, Room> getRooms() {
        return maps.get(DEFAULT_MAP_ID);
    }

    //
    public static HashMap<String, HashMap<String, Room>> getMaps() {
        return maps;
    }
}
