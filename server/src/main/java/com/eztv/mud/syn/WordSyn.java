package com.eztv.mud.syn;

import com.eztv.mud.Word;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Room;

public class WordSyn {
    //地图中GameObject进出房间
    public synchronized static void InOutRoom(GameObject player, String roomId, boolean inOrOut){
        Room room = Word.getInstance().getRooms().get(roomId);
        if (room!=null)
        if(inOrOut){
            room.add(player);
        }else {
            room.remove(player);
        }
    }
}
