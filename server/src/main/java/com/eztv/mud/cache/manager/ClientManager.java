package com.eztv.mud.cache.manager;

import com.eztv.mud.Constant;
import com.eztv.mud.bean.Client;

public class ClientManager {
    public static Client getClient(Object key){
        for (Client client: Constant.clients){
            if(client.getPlayer().getKey().equals(key))
                return client;
        }
        return null;
    }

}
