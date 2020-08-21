package com.eztv.mud.syn;

import com.eztv.mud.Constant;
import com.eztv.mud.GameUtil;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Room;
import com.eztv.mud.cache.RoomCache;

public class WordSyn {
    //地图中GameObject进出房间
    public synchronized static void InOutRoom(GameObject player, Room targetRoom, boolean inOrOut){
        if(targetRoom==null){
            targetRoom = GameUtil.getRoom(Constant.DEFAULT_MAP_ID,Constant.DEFAULT_ROOM_ID);
        }
        Room room = RoomCache. getRoomsByMap(targetRoom.getMap()+"").get(targetRoom.getId()+"");
        if (room!=null)
        if(inOrOut){
            room.add(player);
        }else {
            room.remove(player);
        }
    }
}
