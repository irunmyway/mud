package com.eztv.mud.syn;

import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Room;
import com.eztv.mud.cache.RoomCache;

public class WordSyn {
    //地图中GameObject进出房间
    public synchronized static void InOutRoom(GameObject player, String roomId, boolean inOrOut){
        Room room = RoomCache.getRooms().get(roomId);
        if (room!=null)
        if(inOrOut){
            room.add(player);
        }else {
            room.remove(player);
        }
    }
}
