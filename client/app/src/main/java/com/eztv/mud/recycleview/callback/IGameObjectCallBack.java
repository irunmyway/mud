package com.eztv.mud.recycleview.callback;

import android.view.View;

import com.eztv.mud.bean.GameObject;
import com.eztv.mud.bean.Npc;
import com.eztv.mud.bean.SendGameObject;

public interface IGameObjectCallBack {
    void onClick(View view, SendGameObject obj);
}
