package com.eztv.mud.bean.callback;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.GameObject;

public interface IGameObject {
    void onRefresh(Client client);
}
