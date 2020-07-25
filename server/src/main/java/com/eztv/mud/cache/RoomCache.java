package com.eztv.mud.cache;

import com.eztv.mud.DataBase;
import com.eztv.mud.bean.Room;
import com.eztv.mud.utils.BDebug;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.List;

import static com.eztv.mud.Constant.Room_PATH;

public class RoomCache {
    public static HashMap<String, Room> Rooms = new HashMap<String, Room>();
    private static HashMap<String, LuaValue> roomScript = new HashMap<String, LuaValue>();
    public static void initRooms() {//初始化所有房间
        //初始化所有房间
        Rooms.clear();
        roomScript.clear();
        List<Room> roomList = DataBase.getInstance().init().createSQL("select * from t_map").list(Room.class);
        for (Room room : roomList) {
            try {
                if (room.getScript().length() > 0) {//模板
                    room.setScript(Room_PATH + room.getScript());
//                    itemScript.put(room.getId() + "", globals.loadfile(room.getScript() + ".lua"));
                }
                Rooms.put(room.getId() + "", room);
            } catch (Exception e) {e.printStackTrace();}
        }
        BDebug.trace("地图加载完成 数量 : Map load mapNum:【" + Rooms.size() + "】");
    }
}
