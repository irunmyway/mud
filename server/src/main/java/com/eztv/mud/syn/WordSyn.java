package com.eztv.mud.syn;

import com.eztv.mud.Word;
import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.net.Player;

public class WordSyn {
    //地图中GameObject进出房间
    public synchronized static void InOutRoom(GameObject player, String roomId, boolean inOrOut){
        if(inOrOut){
            Word.getInstance().getRooms().get(roomId).add(player);
        }else {
            Word.getInstance().getRooms().get(roomId).remove(player);
        }
    }
}
